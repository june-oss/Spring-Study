package com.june.tobyspring.service;

import com.june.tobyspring.domain.User;

public interface UserService {
    void add(User user);
    void upgradeLevels();
}
