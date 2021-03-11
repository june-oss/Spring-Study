package com.june.tobyspring.user.service;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import com.june.tobyspring.user.dao.AppConfiguration;
import com.june.tobyspring.user.domain.Level;
import com.june.tobyspring.user.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;


//@SpringJUnitConfig(AppConfiguration.class)
public class UserTest {
    User user;

    @BeforeEach
    public void setUp(){
        user = new User();
    }

    @Test
    public void upgradeLevel(){
        Level[] levels = Level.values();
        for(Level level : levels) {
            if(level.nextLevel() == null) continue;
            user.setLevel(level);
            user.upgradeLevel();

            assertThat(user.getLevel(), is(level.nextLevel()));
        }
    }

    @Test
    public void cannotUpgradeLevel() {
        Level[] levels = Level.values();
        for(Level level : levels) {
            if(level.nextLevel() != null) continue;
            user.setLevel(level);

            Assertions.assertThrows(IllegalStateException.class, () -> {
                user.upgradeLevel();
            });
        }
    }
}
