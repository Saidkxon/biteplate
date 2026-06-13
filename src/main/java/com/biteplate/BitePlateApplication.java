package com.biteplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.biteplate.domain")
@EnableJpaRepositories(basePackages = "com.biteplate.repository")
public class BitePlateApplication {
    public static void main(String[] args) {
        SpringApplication.run(BitePlateApplication.class, args);
    }
}
