package com.example.hsap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HsapAccountingBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(HsapAccountingBeApplication.class, args);
    }

}
