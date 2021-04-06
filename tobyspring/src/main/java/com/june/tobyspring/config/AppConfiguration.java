package com.june.tobyspring.config;

import com.june.tobyspring.dao.UserDaoJdbc;
import com.june.tobyspring.service.DummyMailSender;
import com.june.tobyspring.service.UserServiceImpl;
import com.june.tobyspring.sqlservice.SimpleSqlService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableAspectJAutoProxy
@EnableTransactionManagement
public class AppConfiguration {

    @Bean
    public DataSource dataSource(){
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost/study?serverTimezone=UTC");
        dataSource.setUsername("toby");
        dataSource.setPassword("1234");

        return dataSource;
    }

    @Bean
    public UserDaoJdbc userDAO(){
        UserDaoJdbc userDAO = new UserDaoJdbc();
        userDAO.setDataSource(dataSource());
        userDAO.setSqlService(sqlService());
        return userDAO;
    }

    @Bean
    public SimpleSqlService sqlService(){
        SimpleSqlService sqlService = new SimpleSqlService();
        Map<String, String> sqlMap = new HashMap<>();
        sqlMap.put("userAdd", "insert into users(id, name, password, email, level, login, recommend) values(?,?,?,?,?,?,?)");
        sqlMap.put("userGet","select * from users where id = ?");
        sqlMap.put("userGetAll","select * from users order by id");
        sqlMap.put("userDeleteAll","delete from users");
        sqlMap.put("userGetCount","select count(*) from users");
        sqlMap.put("userUpdate","update users set name = ?, password = ?, email = ?, level = ?, login = ?, recommend = ? where id =?");
        sqlService.setSqlMap(sqlMap);
        return sqlService;
    }
    @Bean
    public UserServiceImpl userService(){

        UserServiceImpl userService = new UserServiceImpl();
        userService.setUserDao(userDAO());
        userService.setMailSender(mailSender());

        return userService;
    }


    @Bean
    public PlatformTransactionManager transactionManager(){

        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());

        return transactionManager;
    }

//    @Bean
//    public JavaMailSenderImpl mailSender(){
//        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
//        javaMailSender.setHost("mail.server.com");
//
//        return javaMailSender;
//    }

    @Bean //forTest
    public DummyMailSender mailSender(){
        DummyMailSender dummyMailSender = new DummyMailSender();
        return dummyMailSender;
    }

}
