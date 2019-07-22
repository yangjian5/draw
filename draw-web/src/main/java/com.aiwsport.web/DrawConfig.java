package com.aiwsport.web;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yangjian
 */
@Configuration
public class DrawConfig {
    @Bean
    public ObjectMapper drawMapper() {
        return new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
                .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }
}
