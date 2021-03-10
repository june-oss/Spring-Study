package com.june.tobyspring.dao;

import com.june.tobyspring.domain.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import javax.security.auth.login.AccountNotFoundException;
import javax.sql.DataSource;
import java.sql.*;

public class UserDAO {

//    private SimpleConnectionMaker SimpleConnectionMakernectionMaker;
    //인터페이스 이용
    private DataSource dataSource; //초기에 설정하면 사용중에 변화지 않는 읽기전용 인스턴스 변수.
    private JdbcContext jdbcContext;
    private JdbcTemplate jdbcTemplate;

    public UserDAO(){};

    public UserDAO(DataSource dataSource){
//        simpleConnectionMaker = new SimpleConnectionMaker();
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        this.dataSource = dataSource;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setDataSource(DataSource dataSource) {
        this.jdbcContext = new JdbcContext();

        this.jdbcContext.setDataSource(dataSource);

        this.dataSource = dataSource;
    }

    public void setJdbcContext(JdbcContext jdbcContext){
        this.jdbcContext = jdbcContext;
    }

    public void add(final User user) throws SQLException{
        this.jdbcTemplate.update("insert into users(id, name, password) values(?,?,?)",
                user.getId(), user, user.getName(), user.getPassword());
    }

    public User get(String id) throws SQLException{

        return this.jdbcTemplate.queryForObject("select * from users where id = ?",
                new Object[]{id},
                new RowMapper<User>() {
                    @Override
                    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                        User user = new User();
                        user.setId(rs.getString("id"));
                        user.setName(rs.getString("name"));
                        user.setPassword(rs.getString("password"));
                        return user;
                    }
                })
////        Connection c = simpleConnectionMaker.makeNewConnection();
//        Connection c = dataSource.getConnection();
//
//        PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
//        ps.setString(1, id);
//
//        ResultSet rs = ps.executeQuery();
//        User user = null;
//        if(rs.next()){
//            user = new User();
//            user.setId(rs.getString("id"));
//            user.setName(rs.getString("name"));
//            user.setPassword(rs.getString("password"));
//        }
//
//        rs.close();
//        ps.close();
//        c.close();
//
//        if (user == null) throw new EmptyResultDataAccessException(1);
//
//        return user;

    }

    public void deleteAll() throws SQLException{
        this.jdbcTemplate.update("delete from users");
//        this.jdbcTemplate.update(
//                new PreparedStatementCreator() {
//                    @Override
//                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//                        return con.prepareStatement("delete from users");
//                    }
//                }
//        );
    }


    public int getCount() throws SQLException{
        //queryForInt is duplicated!
        return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);

//        return this.jdbcTemplate.query(new PreparedStatementCreator() {
//            @Override
//            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//                return con.prepareStatement("select count(*) from users");
//            }
//        }, new ResultSetExtractor<Integer>() {
//            @Override
//            public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
//                rs.next();
//                return rs.getInt();
//            }
//        });
    }

//    //JdbcContext에 public method로 옮겼다.
//    public void executeSql(final String query) throws SQLException{
//        this.jdbcContext.workWithStatementStrategy(
//            new StatementStrategy() {
//                @Override
//                public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
//                    return c.prepareStatement(query);
//                }
//            }
//        );
//    }

//    //JdbcContext class로 분리시킴
//    public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException{
//        Connection c = null;
//        PreparedStatement ps = null;
//
//        try{
//            c = dataSource.getConnection();
//
//            ps = stmt.makePreparedStatement(c);
//
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            throw e;
//        } finally {
//            if (ps != null){ try { ps.close(); } catch (SQLException e) {} }
//            if (c != null){ try { c.close(); } catch (SQLException e) {} }
//        }
//    }

//    private PreparedStatement makeStatement(Connection c) throws  SQLException{
//        PreparedStatement ps;
//        ps = c.prepareStatement("delete from users");
//        return ps;
//    }

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
