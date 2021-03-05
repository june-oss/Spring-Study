package com.june.tobyspring.dao;

import com.june.tobyspring.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

public class UserDAOTest {

    @Test
    public void addAndGet() throws SQLException {
//        ConnectionMaker connectionMaker = new DConnectionMaker();
//        UserDAO dao = new DaoFactory().userDAO();

        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDAO dao = context.getBean("userDAO", UserDAO.class);

        User user = new User();
        user.setId("mlicp");
        user.setName("이현중");
        user.setPassword("1234");

        dao.add(user);


    }
}
