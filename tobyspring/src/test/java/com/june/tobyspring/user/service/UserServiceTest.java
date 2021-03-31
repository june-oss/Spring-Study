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
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

@SpringJUnitConfig(AppConfiguration.class)
public class UserServiceTest {
    @Autowired private UserService userService;
    @Autowired private UserDaoJdbc userDao;
    @Autowired
    PlatformTransactionManager transactionManager;
    @Autowired
    MailSender mailSender;
    List<User> users;

    @BeforeEach
    public void setUp(){
        users = Arrays.asList(
                new User("bumjin", "박범진", "p1", "hj1@wafour.com", Level.BASIC, UserService.MIN_LOGCOUNT_FOR_SILVER-1, 0),
                new User("joytouch", "강명성", "p2", "hj2@wafour.com", Level.BASIC, UserService.MIN_LOGCOUNT_FOR_SILVER, 0),
                new User("erwins", "신승한", "p3", "hj3@wafour.com", Level.SILVER, 60, UserService.MIN_RECOMMEND_FOR_GOLD-1),
                new User("madnite1", "이상호", "p4", "hj4@wafour.com", Level.SILVER, 60, UserService.MIN_RECOMMEND_FOR_GOLD),
                new User("green", "오민규", "p5", "hj5@wafour.com", Level.GOLD, 100, Integer.MAX_VALUE)
        );
    }

    @Test
    public void bean(){
        assertThat(this.userService, is(notNullValue()));
    }

    @Test
    public void upgradeLevels() throws Exception {
        userDao.deleteAll();
        for(User user : users) userDao.add(user);

        userService.upgradeLevels();

        checkLevel(users.get(0), false);
        checkLevel(users.get(1), true);
        checkLevel(users.get(2), false);
        checkLevel(users.get(3), true);
        checkLevel(users.get(4), false);
    }

    private void checkLevel(User user, Boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if(upgraded)
            assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
        else
            assertThat(userUpdate.getLevel(), is(user.getLevel()));
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

    static class TestUserService extends UserService{
        private  String id;

        private TestUserService(String id){
            this.id = id;
        }

        @Override
        protected void upgradeLevel(User user) {
            if (user.getId().equals(this.id))
                throw new TestUserServiceException();
            super.upgradeLevel(user);
        }

        static class TestUserServiceException extends RuntimeException {
        }
    }

    @Test
    public void upgradeAllOrNothing() throws Exception {
        UserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.userDao);
        testUserService.setTransactionManager(transactionManager);
        testUserService.setMailSender(mailSender);
        
        userDao.deleteAll();
        for(User user : users) userDao.add(user);

        try {
            testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        }
        catch (TestUserService.TestUserServiceException e){}

        checkLevel(users.get(1),false);
    }
}

