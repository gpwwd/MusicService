package com.musicservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;

@SpringBootApplication
public class MusicserviceApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(MusicserviceApplication.class, args);
    }

}
