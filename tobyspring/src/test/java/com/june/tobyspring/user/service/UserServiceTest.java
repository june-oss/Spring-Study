package com.june.tobyspring.user.service;

import com.june.tobyspring.user.dao.AppConfiguration;
import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(AppConfiguration.class)
public class UserServiceTest {
    @Autowired private UserService userService;

    @Test
    public void bean(){
        assertThat(this.userService, is(notNullValue()));
    }
}
