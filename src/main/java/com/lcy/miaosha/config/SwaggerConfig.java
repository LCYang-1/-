package com.lcy.miaosha.config;


import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket productApi() {
        //添加ApiOperiation注解的被扫描.paths(PathSelectors.any()).build();
        //添加ApiOperiation注解的被扫描
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()//.apis(RequestHandlerSelectors.basePackage("com.lcy.miaosha.controller")).paths(PathSelectors.any()).build();
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();


//        return new Docket(DocumentationType.SWAGGER_2)
//                .pathMapping("/")
//                .select()
//                .apis(RequestHandlerSelectors.withClassAnnotation(ApiOperation.class)
//                .paths(PathSelectors.any())
//                .build().apiInfo(new ApiInfoBuilder()
//                        .title("SpringBoot整合Swagger")
//                        .description("SpringBoot整合Swagger，初步使用Swagger")
//                        .version("1.0")
//                        .contact(new Contact("LCY","https://blog.csdn.net/linchaoyang_","3359414533@qq.com"))
//                        .license("The Apache License")
//                        .licenseUrl("http://www.baidu.com")
//                        .build());
//
//
//    }

    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("swagger和springBoot整合")
                .description("swagger的API文档").version("1.0").build();
}




}
