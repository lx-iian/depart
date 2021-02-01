package com.james.depart;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import springfox.documentation.oas.annotations.EnableOpenApi;

import java.io.IOException;
import java.util.TimeZone;

/**
 * @author james
 */
@SpringBootApplication
@EnableOpenApi
public class DepartApplication {

    // http://localhost:${server.port}/swagger-ui/index.html

    public static void main(String[] args) {
        SpringApplication.run(DepartApplication.class, args);
    }

}
