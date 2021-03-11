package com.june.tobyspring.user.service;

import com.june.tobyspring.user.dao.AppConfiguration;
import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import com.june.tobyspring.user.dao.UserDao;
import com.june.tobyspring.user.dao.UserDaoJdbc;
import com.june.tobyspring.user.domain.Level;
import com.june.tobyspring.user.domain.User;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;
import java.util.List;

@SpringJUnitConfig(AppConfiguration.class)
public class UserServiceTest {
    @Autowired private UserService userService;
    @Autowired private UserDaoJdbc userDao;
    List<User> users;

    @BeforeEach
    public void setUp(){
        users = Arrays.asList(
                new User("bumjin", "박범진", "p1", Level.BASIC, 49, 0),
                new User("joytouch", "강명성", "p2", Level.BASIC, 50, 0),
                new User("erwins", "신승한", "p3", Level.SILVER, 60, 29),
                new User("madnite1", "이상호", "p4", Level.SILVER, 60, 30),
                new User("green", "오민규", "p5", Level.GOLD, 100, 100)
        );
    }

    @Test
    public void bean(){
        assertThat(this.userService, is(notNullValue()));
    }

    @Test
    public void upgradeLevels() {
        userDao.deleteAll();
        for(User user : users) userDao.add(user);

        userService.upgradeLevels();

        checkLevel(users.get(0), Level.BASIC);
        checkLevel(users.get(1), Level.SILVER);
        checkLevel(users.get(2), Level.SILVER);
        checkLevel(users.get(3), Level.GOLD);
        checkLevel(users.get(4), Level.GOLD);
    }

    private void checkLevel(User user, Level expectedLevel) {
        User userUpdate = userDao.get(user.getId());
        assertThat(userUpdate.getLevel(), is(expectedLevel));
    }

    @Test
    public void add() {
        userDao.deleteAll();

        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevel.getLevel(), is(userWithLevelRead.getLevel()));
        assertThat(userWithoutLevel.getLevel(), is(Level.BASIC));

    }

}
