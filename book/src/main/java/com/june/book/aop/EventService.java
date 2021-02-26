package com.june.book.aop;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public interface EventService {

    void createEvent();

    void publishEvent();

    public void deleteEvent();
}
