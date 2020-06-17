package com.lcy.miaosha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
public class MiaoshaApplication  {

    public static void main(String[] args) {
        SpringApplication.run(MiaoshaApplication.class, args);
    }

//    //extends SpringBootServletInitializer
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(MiaoshaApplication.class);
//    }
}
