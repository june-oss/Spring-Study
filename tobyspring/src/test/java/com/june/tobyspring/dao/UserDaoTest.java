package com.june.tobyspring.dao;

import com.june.tobyspring.config.AppConfiguration;
import com.june.tobyspring.domain.Level;
import com.june.tobyspring.domain.User;
import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

@SpringJUnitConfig(AppConfiguration.class)//@ContextConfiguration
public class UserDaoTest {
    @Autowired
    private UserDaoJdbc dao;
    private User user1, user2, user3;

    private void checkSameValue(User user1, User user2) {
        assertThat(user1.getId(), is(user2.getId()));
        assertThat(user1.getName(), is(user2.getName()));
        assertThat(user1.getPassword(), is(user2.getPassword()));
        assertThat(user1.getEmail(), is(user2.getEmail()));
        assertThat(user1.getLevel(), is(user2.getLevel()));
        assertThat(user1.getName(), is(user2.getName()));
        assertThat(user1.getPassword(),is(user2.getPassword()));
    }

    @BeforeEach //junit @Before와 동일 -- 각 테스트 메서드 전에 실행된다.
    public void setUp(){
        this.user1 = new User("aaa", "최은정", "springno1", "hj@wafour.com", Level.BASIC, 1, 0);
        this.user2 = new User("bbb", "이현중", "springno2", "hj@wafour.com", Level.SILVER, 55 ,10);
        this.user3 = new User("ccc", "양우진", "springno3", "hj@wafour.com", Level.GOLD, 100,40);
    }

    @Test
    void addAndGet() {
        dao.deleteAll();
        assertEquals(dao.getCount(), 0);

        dao.add(user1);
        dao.add(user2);
        assertEquals(dao.getCount(), 2);

        User userget1 = dao.get(user1.getId());
        checkSameValue(user1, userget1);

        User userget2 = dao.get(user2.getId());
        checkSameValue(user2, userget2);
    }

    @Test
    public void count() {
        dao.deleteAll();
        assertEquals(dao.getCount(), 0);

        dao.add(user1);
        assertEquals(dao.getCount(), 1);

        dao.add(user2);
        assertEquals(dao.getCount(), 2);

        dao.add(user3);
        assertEquals(dao.getCount(), 3);
    }

    @Test
    public void getUserFailure() {
        dao.deleteAll();
        assertEquals(dao.getCount(), 0);

        assertThrows(EmptyResultDataAccessException.class, () -> {
            User user = dao.get("unknown_id");
        });
    }

    @Test
    public void getAll() {
        dao.deleteAll();

        List<User> users0 = dao.getAll();
        assertThat(users0.size(), is(0));

        dao.add(user1);
        List<User> users1 = dao.getAll();
        assertThat(users1.size(), is(1));
        checkSameValue(user1, users1.get(0));

        dao.add(user2);
        List<User> users2 = dao.getAll();
        assertThat(users2.size(), is(2));
        checkSameValue(user1, users2.get(0));
        checkSameValue(user2, users2.get(1));

        dao.add(user3);
        List<User> users3 = dao.getAll();
        assertThat(users3.size(), is(3));
        checkSameValue(user1, users3.get(0));
        checkSameValue(user2, users3.get(1));
        checkSameValue(user3, users3.get(2));
    }

    @Test
    public void duplicateKey(){
        dao.deleteAll();

        dao.add(user1);
        assertThrows(DataAccessException.class,() -> {
            dao.add(user1);
        });
    }

    @Test
    public void update() {
        dao.deleteAll();

        dao.add(user1);
        dao.add(user2);

        user1.setName("오민규");
        user1.setPassword("springno6");
        user1.setEmail("asdf@wafour.com");
        user1.setLevel(Level.GOLD);
        user1.setLogin(1000);
        user1.setRecommend(999);
        dao.update(user1);

        User user1update = dao.get(user1.getId());
        checkSameValue(user1, user1update);
        User user2same = dao.get(user2.getId());
        checkSameValue(user2, user2same);
    }

}
