package com.aiwsport.web;

import com.aiwsport.web.interceptor.AccessHandlerInteceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication
@ComponentScan(basePackages = {"com.aiwsport.web", "com.aiwsport.core"})
@MapperScan(basePackages = {"com.aiwsport.core.mapper"})
@EnableTransactionManagement
public class WebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

    @Configuration
    static class WebMvc implements WebMvcConfigurer {
        //增加拦截器
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new AccessHandlerInteceptor())    //指定拦截器类
                    .addPathPatterns("/api/backend/**");        //指定该类拦截的url
        }

        /**
         * 设置头 使可以跨域访问
         * @param registry
         * @since 4.2
         */
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("*")
                    .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                    .maxAge(60 * 60 * 6)
                    .allowCredentials(true);
        }
    }

}