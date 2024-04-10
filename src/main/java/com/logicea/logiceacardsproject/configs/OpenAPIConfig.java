package com.logicea.logiceacardsproject.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Value("${server.port}")
    private String devPort;

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:" + devPort);
        devServer.setDescription("Dev URL environment");

        Contact contact = new Contact();
        contact.setEmail("harrisonkanda@gmail.com");
        contact.setName("Developer Playground");
        contact.setUrl("");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Card API Project - by Logicea")
                .version("1.0")
                .contact(contact)
                .description("Exposes Cards APIs for running CRUD operations on cards").termsOfService("For Interviewing..:)")
                .license(mitLicense);

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearer-key",
                 new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .info(info).servers(List.of(devServer));
    }

}

