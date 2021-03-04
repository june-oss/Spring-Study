package com.june.tobyspring.dao;

import com.june.tobyspring.domain.User;

import java.sql.SQLException;

public class UserDAOTest {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        ConnectionMaker connectionMaker = new DConnectionMaker();

        UserDAO dao = new UserDAO(connectionMaker);

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
    }
}
