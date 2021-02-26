package com.june.book;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    //IOC 의존성 주입
    @Bean
    public  BookRepository bookRepository(){
        return new BookRepository();
    }

    //의존성주입을 하지 않고 @Autowird사용
    @Bean
    public BookService bookService(){
        return new BookService();
    }
//    //의존성 주입사용
//    @Bean
//    public BookService bookService(){
//        BookService bookService = new BookService();
//        bookService.setBookRepository(bookRepository());
//        return bookService;
//    }
}
