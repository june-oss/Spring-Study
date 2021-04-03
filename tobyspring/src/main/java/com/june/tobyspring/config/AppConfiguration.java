package com.june.tobyspring.config;

import com.june.tobyspring.dao.UserDaoJdbc;
import com.june.tobyspring.handler.TransactionAdvice;
import com.june.tobyspring.service.DummyMailSender;
import com.june.tobyspring.service.UserServiceImpl;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import springbook.learningtest.jdk.proxy.NameMatchClassMethodPointcut;

import javax.sql.DataSource;

@Configuration
@EnableAspectJAutoProxy
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
    public DataSourceTransactionManager transactionManager(){

        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());

        return transactionManager;
    }

    @Bean
    public TransactionAdvice transactionAdvice(){
        TransactionAdvice transactionAdvice = new TransactionAdvice();
        transactionAdvice.setTransactionManager(transactionManager());
        return transactionAdvice;
    }

    @Bean
    public AspectJExpressionPointcut transactionPointcut(){
        AspectJExpressionPointcut aspectJExpressionPointcut = new AspectJExpressionPointcut();
        aspectJExpressionPointcut.setExpression("execution(* *..*ServiceImpl .upgrade*(,,))");

        return aspectJExpressionPointcut;
    }

    @Bean
    public DefaultBeanFactoryPointcutAdvisor transactionAdvisor(){
        DefaultBeanFactoryPointcutAdvisor pointcutAdvisor = new DefaultBeanFactoryPointcutAdvisor();
        pointcutAdvisor.setAdvice(transactionAdvice());
        pointcutAdvisor.setPointcut(transactionPointcut());
        return pointcutAdvisor;
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
