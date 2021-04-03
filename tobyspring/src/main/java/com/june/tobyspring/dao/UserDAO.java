package com.june.tobyspring.dao;

import com.june.tobyspring.domain.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.security.auth.login.AccountNotFoundException;
import javax.sql.DataSource;
import java.sql.*;

public class UserDAO {
    private DataSource dataSource;

    public UserDAO(){};

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public UserDAO(DataSource dataSource){
        this.dataSource = dataSource;
    }


    public void add(User user) throws SQLException{
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
    }

    public User get(String id) throws SQLException{
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        User user = null;
        if(rs.next()){
            user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
        }

        rs.close();
        ps.close();
        c.close();

        if (user == null) throw new EmptyResultDataAccessException(1);

        return user;
    }

    public void deleteAll() throws SQLException{
        //예외가 발생하도 리소스를 반환하게 만들어야한다.
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();
            ps = c.prepareStatement("delete from users");
            ps.executeUpdate();
        } catch (SQLException e){
            throw e;
        } finally {
            if( ps != null){
                try {
                    ps.close();
                } catch (SQLException e){}
            }
            if( c != null){
                try {
                    c.close();
                } catch (SQLException e) {}
            }
        }
        //use try resourc with
//        try(Connection c = dataSource.getConnection();
//            PreparedStatement ps = c.prepareStatement("delete from users")){
//            ps.executeUpdate();
//        } catch (SQLException e){
//            throw e;
//        }
    }

    public int getCount() throws SQLException{
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement("select count(*) from users");

        ResultSet rs = ps.executeQuery();
        rs.next();
        int count = rs.getInt(1);

        rs.close();
        ps.close();
        c.close();

        return count;
    }
}
