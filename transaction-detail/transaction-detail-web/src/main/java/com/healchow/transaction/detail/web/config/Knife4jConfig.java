package com.healchow.transaction.detail.web.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j API Configuration
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public GroupedOpenApi systemApi() {
        return createRestApi("System-Manager", "com.healchow.transaction.detail.web.controller.system");
    }

    /**
     * Create RestApi
     *
     * @param groupName group name
     * @param basePackage package path
     * @return instance of GroupedOpenApi
     */
    public GroupedOpenApi createRestApi(String groupName, String basePackage) {
        return GroupedOpenApi.builder()
                .group(groupName)
                .packagesToScan(basePackage)
                .pathsToMatch("/**")
                .addOpenApiCustomizer(openApi -> openApi.info(apiInfo()))
                .build();
    }

    /**
     * API info
     *
     * @return API info
     */
    private Info apiInfo() {
        return new Info()
                .title("Transaction Manager API Docs")
                .description("Transaction Manager Service")
                .version("1.0.0")
                .contact(new Contact().name("HealChow").url("https://github.com/healchow").email("healchow@gmail.com"))
                .license(new License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0.html"));
    }

}
