package com.example.demo.global.config.web;

import com.example.demo.global.converter.CommentTargetBoardTypeConverter;
import com.example.demo.global.converter.EntireBoardTypeConverter;
import com.example.demo.global.converter.RecruitmentBoardTypeConverter;
import com.example.demo.global.converter.StatusConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://ec2-3-27-33-141.ap-southeast-2.compute.amazonaws.com:5000/")
                .allowedOriginPatterns("http://localhost:8080/", "/**")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/docs/**")
                .addResourceLocations("classpath:/static/docs/");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new EntireBoardTypeConverter());
        registry.addConverter(new CommentTargetBoardTypeConverter());
        registry.addConverter(new RecruitmentBoardTypeConverter());
        registry.addConverter(new StatusConverter());
    }
}
