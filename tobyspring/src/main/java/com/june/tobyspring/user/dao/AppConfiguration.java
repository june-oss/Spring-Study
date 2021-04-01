package com.june.tobyspring.user.dao;

import com.june.tobyspring.user.factorybean.TxProxyFactoryBean;
import com.june.tobyspring.user.service.UserService;
import springbook.learningtest.factorybean.MessageFactoryBean;
import com.june.tobyspring.user.service.DummyMailSender;
import com.june.tobyspring.user.service.UserServiceImpl;
import com.june.tobyspring.user.service.UserServiceTx;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

@Configuration
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
    public TxProxyFactoryBean userService(){
        TxProxyFactoryBean txProxyFactoryBean = new TxProxyFactoryBean();
        txProxyFactoryBean.setTarget(userServiceImpl());
        txProxyFactoryBean.setTransactionManager(transactionManager());
        txProxyFactoryBean.setPattern("upgradeLevels");
        txProxyFactoryBean.setServiceInterface(UserService.class);
        return txProxyFactoryBean;
    }

    @Bean
    public UserServiceImpl userServiceImpl(){

        UserServiceImpl userService = new UserServiceImpl();
        userService.setUserDao(userDAO());
        userService.setMailSender(mailSender());

        return userService;
    }

    @Bean
    public DataSourceTransactionManager transactionManager(){

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
