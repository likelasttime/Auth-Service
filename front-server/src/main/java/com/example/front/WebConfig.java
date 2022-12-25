package com.example.front;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/").setViewName("index.html");
        registry.addViewController("/authentication").setViewName("authentication.html");
        registry.addViewController("/login").setViewName("login.html");
        registry.addViewController("/passwordMail").setViewName("passwordMail.html");
        registry.addViewController("/signup").setViewName("signup.html");
        registry.addViewController("/updatePassword").setViewName("updatePassword.html");
        registry.addViewController("/verify").setViewName("verify.html");
    }
}
