package com.june.book.nullsafety;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class EventService2 {

    @NonNull
    public String creatEvent(@NonNull String name){
        return  null;
    }

}
