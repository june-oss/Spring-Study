package com.june.tobyspring.config;

import com.june.tobyspring.dao.UserDaoJdbc;
import com.june.tobyspring.service.DummyMailSender;
import com.june.tobyspring.service.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

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

        return userDAO;
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
