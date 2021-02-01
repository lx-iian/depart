package com.james.depart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * @author james
 */
@SpringBootApplication
@EnableOpenApi
public class DepartApplication {

    public static void main(String[] args) {
        SpringApplication.run(DepartApplication.class, args);
    }

}
