package com.healchow.transaction.detail.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties
@ComponentScan(basePackages = "com.healchow.transaction.detail")
public class TransactionDetailApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactionDetailApplication.class, args);
        log.info("Transaction Detail Application Starts Successfully!");
    }

}
