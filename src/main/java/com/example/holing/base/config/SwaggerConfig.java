package com.example.holing.base.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "LikeLion Hackathon Holing API 문서",
                description = "Holing API 명세에 관련된 문서입니다.",
                version = "v1")
)
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi openApi() {
        String[] paths = {"/**"};

        return GroupedOpenApi.builder()
                .group("Holing API v1")
                .pathsToMatch(paths)
                .build();
    }

}