package com.healchow.transaction.detail.web.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    /**
     * Whitelist for static path
     */
    private static final String[] STATIC_PATH_WHITELIST = new String[]{
            "/",
            "/js/**",
            "/css/**",
            "/img/**",
            "/fonts/**",
            "/index.html",
            "/favicon.ico",
            "/doc.html",
            "/swagger-ui.html",
            "/webjars/**",
            "/swagger-resources/**",
            "/v3/**"
    };

    /**
     * Whitelist for static resources
     */
    private static final String[] STATIC_LOCATION_WHITELIST = new String[]{
            "classpath:/static/",
            "classpath:/public/",
            "classpath:/META-INF/resources/",
            "classpath:/META-INF/resources/webjars/",
    };

    @Bean
    public ObjectMapper objectMapper() {
        JsonMapper.Builder builder = JsonMapper.builder();

        builder.serializationInclusion(JsonInclude.Include.NON_NULL);
        builder.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        builder.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        builder.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        builder.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);

        builder.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);

        return builder.build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(STATIC_PATH_WHITELIST)
                .addResourceLocations(STATIC_LOCATION_WHITELIST)
                .setCachePeriod(0);
    }

}