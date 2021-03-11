package com.june.tobyspring.user.service;

import com.june.tobyspring.user.dao.UserDao;
import com.june.tobyspring.user.domain.Level;
import com.june.tobyspring.user.domain.User;

import java.util.List;
import java.util.TreeMap;

public class UserService {
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;
    UserDao userDao;

    public void setUserDao(UserDao userDao){
        this.userDao = userDao;
    }

    public void upgradeLevels(){
        List<User> users = userDao.getAll();
        for(User user : users){
            if(canUpgradeLevel(user))
                upgradeLevel(user);
        }
    }

    private void upgradeLevel(User user) {
        //User Class로 이동시킴
//        if(user.getLevel() == Level.BASIC) user.setLevel(Level.SILVER);
//        else if(user.getLevel() == Level.SILVER) user.setLevel(Level.GOLD);

        user.upgradeLevel();
        userDao.update(user);
    }

    private boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
            case SILVER: return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unknown Level: " + currentLevel);
        }
    }

    public void add(User user) {
        if (user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }
}
