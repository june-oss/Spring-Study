package com.june.tobyspring.dao;

import com.june.tobyspring.dao.strategy.AddStatement;
import com.june.tobyspring.dao.strategy.DeleteAllStatement;
import com.june.tobyspring.dao.strategy.StatementStrategy;
import com.june.tobyspring.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;

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
        StatementStrategy st = new AddStatement(user);
        jdbcContextWithStatementStrategy(st);
    }

    public User get(String id) throws SQLException{
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement("select * from usersTest where id = ?");
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
        StatementStrategy st = new DeleteAllStatement(); //전략클래스의 오브젝트 생성
        jdbcContextWithStatementStrategy(st);   //컨텍스트 호출 및 전략 오브젝트 전달
        //비록 클라이언트와 컨텍스트는 클래스를 분리하지 않았지만, 의존관계와 책임으로 볼 때 이상적인 관계를 가지고있다.
    }

    public int getCount() throws SQLException{
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement("select count(*) from usersTest");

        ResultSet rs = ps.executeQuery();
        rs.next();
        int count = rs.getInt(1);

        rs.close();
        ps.close();
        c.close();

        return count;
    }

    public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException{
        Connection c = null;
        PreparedStatement ps = null;

        try{
            c = dataSource.getConnection();

            ps = stmt.makePreparedStatement(c);

            ps.executeUpdate();
        }  catch (SQLException e){
            throw e;
        } finally {
            if( ps != null){ try { ps.close(); } catch (SQLException e){} }
            if( c != null){ try { c.close(); } catch (SQLException e) {} }
        }
    }
}