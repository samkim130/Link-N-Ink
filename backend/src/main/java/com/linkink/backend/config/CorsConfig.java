package com.linkink.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//https://stackoverflow.com/questions/32319396/cors-with-spring-boot-and-angularjs-not-working
//@Configuration
public class CorsConfig {
    // will be used eventually

        //implements WebMvcConfigurer{
  /*
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE").allowedOrigins("*").allowedHeaders("*");
    }

   */
}
