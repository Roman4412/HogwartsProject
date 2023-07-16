package ru.hogwarts.school;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@OpenAPIDefinition
public class HogwartsProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(HogwartsProjectApplication.class, args);
    }

}
