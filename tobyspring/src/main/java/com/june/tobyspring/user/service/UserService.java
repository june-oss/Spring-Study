package com.june.tobyspring.user.service;

import com.june.tobyspring.user.dao.UserDao;

public class UserService {
    UserDao userDao;

    public void setUserDao(UserDao userDao){
        this.userDao = userDao;
    }

}
