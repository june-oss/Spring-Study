package com.june.book.nullsafety;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppRunner2 implements ApplicationRunner {

    @Autowired
    EventService2 eventService2;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String message = eventService2.creatEvent("lee");
    }
}
