package com.healchow.transaction.detail.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableAsync
@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties
@ComponentScan(basePackages = "com.healchow.transaction.detail")
public class TransactionDetailApplication {

    public static void main(String[] args) {
        ConfigurableEnvironment env = SpringApplication.run(TransactionDetailApplication.class, args).getEnvironment();
        log.info("""

                        ----------------------------------------------------------
                        \tApplication: '{}' Started.
                        \tService Doc: http://{}:{}{}/doc.html
                        ----------------------------------------------------------""",
                env.getProperty("spring.application.name"),
                env.getProperty("server.address"),
                env.getProperty("server.port"),
                env.getProperty("server.servlet.context-path")
        );
    }

}
