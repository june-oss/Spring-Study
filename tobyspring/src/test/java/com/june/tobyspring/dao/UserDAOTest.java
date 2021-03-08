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

//@ExtendWith(SpringExtension.class)  //@RunWith(SpringJUnitClassRunner.class)
@SpringJUnitConfig(DaoFactory.class)//@ContextConfiguration
//@DirtiesContext //테스트메소드에서 application context구성이나 상태를 변경하는다는 것을 tesxcontext framework에 알려줌
public class UserDAOTest {
//    @Autowired
//    private ApplicationContext context;
//    아래처럼 userDAO를 직접 주입받는다.
    @Autowired
    private UserDAO dao;
    private User user1, user2, user3;

    @BeforeEach //junit @Before와 동일 -- 각 테스트 메서드 전에 실행된다.
    public void setUp(){
        System.out.println("==========================================");
//        System.out.println(this.context);
        System.out.println(this);
        System.out.println("==========================================");

//        this.dao = this.context.getBean("userDAO", UserDAO.class);

        this.user1 = new User("aaa", "최은정", "springno1");
        this.user2 = new User("bbb", "이현중", "springno2");
        this.user3 = new User("ccc", "양우진", "springno3");
    }


    @Test
    void addAndGet() throws SQLException {
//        ConnectionMaker connectionMaker = new DConnectionMaker();
//        UserDAO dao = new DaoFactory().userDAO();
//        User user1 = new User("111", "김현중", "springno1");
//        User user2 = new User("222", "최은정", "springno2");

        dao.deleteAll();

        assertEquals(dao.getCount(), 0);

//        User user = new User();
//        user.setId("mlicp");
//        user.setName("이현중");
//        user.setPassword("1234");

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
//        setUp()메소드로 분리
//        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
//        UserDAO dao = context.getBean("userDAO", UserDAO.class);
//        User user1 = new User("aaa", "최은정", "springno1");
//        User user2 = new User("bbb", "이현중", "springno2");
//        User user3 = new User("ccc", "양우진", "springno3");

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
//        setUp()메소드로 분리
//        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
//        UserDAO dao = context.getBean("userDAO", UserDAO.class);

        dao.deleteAll();
        assertEquals(dao.getCount(), 0);

        assertThrows(EmptyResultDataAccessException.class, () -> {
            User user = dao.get("unknown_id");
        });
    }


}
