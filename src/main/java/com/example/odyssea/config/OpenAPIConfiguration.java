package com.example.odyssea.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Development");

        Info information = new Info()
                .title("Odyssea - Where your next discovery begins")
                .version("1.0")
                .description("The Odyssea API empowers seamless access to personalized travel generation, surprise trip creation, and a curated catalog of ready-to-book luxury itineraries. Designed for effortless integration, it brings meaningful journeys to life—tailored, unexpected, or instantly available.");
        return new OpenAPI().info(information).servers(List.of(server));
    }
}
