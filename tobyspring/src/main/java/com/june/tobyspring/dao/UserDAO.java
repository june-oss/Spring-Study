package com.june.tobyspring.dao;

import com.june.tobyspring.domain.User;

import java.sql.*;

public class UserDAO {

//    private SimpleConnectionMaker SimpleConnectionMakernectionMaker;
    //인터페이스 이용
    private ConnectionMaker connectionMaker;

    public UserDAO(ConnectionMaker connectionMaker){
//        simpleConnectionMaker = new SimpleConnectionMaker();
        this.connectionMaker = connectionMaker;
    }

    public void add(User user) throws ClassNotFoundException, SQLException{
//        Connection c = simpleConnectionMaker.makeNewConnection();
        Connection c = connectionMaker.makeConnection();

        PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
    }

    public User get(String id) throws ClassNotFoundException, SQLException{
//        Connection c = simpleConnectionMaker.makeNewConnection();
        Connection c = connectionMaker.makeConnection();

        PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        c.close();

        return user;
    }

//    //중복 코드의 메소드 추출
//    private Connection getConnection() throws ClassNotFoundException, SQLException{
//        Class.forName("com.mysql.cj.jdbc.Driver");
//        Connection c = DriverManager.getConnection(
//                "jdbc:mysql://localhost/study?serverTimezone=UTC", "toby", "1234");
//
//        return  c;
//    }

    //테스트용 main()메서드
//    public static void main(String[] args) throws ClassNotFoundException, SQLException{
//        UserDAO dao = new UserDAO();
//
//        User user = new User();
//        user.setId("mlicp");
//        user.setName("이현중");
//        user.setPassword("1234");
//
//        dao.add(user);
//
//        System.out.println(user.getId() + " 등록 성공");
//
//        User user2 = dao.get(user.getId());
//        System.out.println(user2.getName());
//        System.out.println(user2.getPassword());
//
//        System.out.println(user2.getId() + " 조회 성공");
//    }
}
