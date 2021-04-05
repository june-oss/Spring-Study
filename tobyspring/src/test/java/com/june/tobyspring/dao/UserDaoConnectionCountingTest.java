package com.june.tobyspring.dao;

import com.june.tobyspring.dao.connectionmaker.CountingConnectionMaker;
import com.june.tobyspring.dao.connectionmaker.CountingDaoFactory;
import com.june.tobyspring.domain.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

public class UserDaoConnectionCountingTest {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);

        UserDaoJdbc dao = context.getBean("userDao", UserDaoJdbc.class);

        User user = new User();
        user.setId("mlicp");
        user.setName("이현중");
        user.setPassword("1234");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId() + " 조회 성공");

        CountingConnectionMaker ccm = context.getBean("connectionMaker",CountingConnectionMaker.class);
        System.out.println("Connection counter : " + ccm.getCounter());
    }
}
