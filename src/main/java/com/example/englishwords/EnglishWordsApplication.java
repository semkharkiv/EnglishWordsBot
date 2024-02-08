package com.example.englishwords;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.example.englishwords.entity")
public class EnglishWordsApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnglishWordsApplication.class, args);
    }
}
