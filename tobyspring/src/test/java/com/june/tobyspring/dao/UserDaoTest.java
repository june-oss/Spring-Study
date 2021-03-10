package com.june.tobyspring.dao;

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

import java.sql.SQLException;
import java.util.List;

@SpringJUnitConfig(DaoFactory.class)//@ContextConfiguration
public class UserDaoTest {
    @Autowired
    private UserDaoJdbc dao;
    private User user1, user2, user3;

    @BeforeEach //junit @Before와 동일 -- 각 테스트 메서드 전에 실행된다.
    public void setUp(){
        System.out.println("==========================================");
        System.out.println(this);
        System.out.println("==========================================");

        this.user1 = new User("aaa", "최은정", "springno1");
        this.user2 = new User("bbb", "이현중", "springno2");
        this.user3 = new User("ccc", "양우진", "springno3");
    }


    @Test
    void addAndGet() {
        dao.deleteAll();
        assertEquals(dao.getCount(), 0);

        dao.add(user1);
        dao.add(user2);
        assertEquals(dao.getCount(), 2);

        User userget1 = dao.get(user1.getId());
        assertEquals(userget1.getName(), user1.getName());
        assertEquals(userget1.getPassword(), user1.getPassword());

        User userget2 = dao.get(user2.getId());
        assertEquals(userget2.getName(), user2.getName());
        assertEquals(userget2.getPassword(), user2.getPassword());
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

    private void checkSameValue(User user1, User user2) {
        assertThat(user1.getId(), is(user2.getId()));
        assertThat(user1.getName(), is(user2.getName()));
        assertThat(user1.getPassword(), is(user2.getPassword()));
    }

    @Test
    public void duplicateKey(){
        dao.deleteAll();

        dao.add(user1);
        assertThrows(DataAccessException.class,() -> {
            dao.add(user1);
        });
    }

}
