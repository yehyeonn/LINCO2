package com.lec.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class LincoApplication {

    public static void main(String[] args) {
        SpringApplication.run(LincoApplication.class, args);
    }

}
