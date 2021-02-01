package com.james.depart.config;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author james
 * @date 2021-01-07
 */
@Configuration
public class Swagger3Config implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(Swagger3Config.class);

    @Value("${server.port}")
    private String port;

    @Override
    public void run(String... args) {
        LOGGER.info("http://localhost:" + port + "/swagger-ui/index.html", "xxx@foxmail.com");
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("我的接口文档")
                .description("部门管理接口文档。")
                .contact(new Contact("james", "http://localhost:" + port + "/swagger-ui/index.html", "xxx@foxmail.com"))
                .version("1.0")
                .build();
    }

}