package com.june.tobyspring.user.dao;

import com.june.tobyspring.user.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.TransactionManager;

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
    public UserService userService(){

        UserService userService = new UserService();
        userService.setUserDao(userDAO());
        userService.setTransactionManager(transactionManager());

        return userService;
    }

    @Bean
    public DataSourceTransactionManager transactionManager(){

        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());

        return transactionManager;
    }

}
