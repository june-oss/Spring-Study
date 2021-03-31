package com.june.tobyspring.user.service;

import com.june.tobyspring.user.domain.User;

public interface UserService {
    void add(User user);
    void upgradeLevels();
}
