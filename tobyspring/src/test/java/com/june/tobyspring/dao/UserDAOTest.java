package com.june.tobyspring.dao;

import com.june.tobyspring.domain.User;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.sql.SQLException;

@SpringJUnitConfig(DaoFactory.class)//@ContextConfiguration
public class UserDAOTest {
    @Autowired
    private UserDAO dao;
    private User user1, user2, user3;

    @BeforeEach
    public void setUp(){
        System.out.println("==========================================");
        System.out.println(this);
        System.out.println("==========================================");

        this.user1 = new User("aaa", "최은정", "springno1");
        this.user2 = new User("bbb", "이현중", "springno2");
        this.user3 = new User("ccc", "양우진", "springno3");
    }


    @Test
    void addAndGet() throws SQLException {
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
    public void count() throws SQLException{
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
    public void getUserFailure() throws SQLException{
        dao.deleteAll();
        assertEquals(dao.getCount(), 0);

        assertThrows(EmptyResultDataAccessException.class, () -> {
            User user = dao.get("unknown_id");
        });
    }
}
