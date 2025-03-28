package com.ginkgooai.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    private static final String COOKIE_AUTH_NAME = "cookieAuth";
    private static final String WORKSPACE_HEADER = "x-workspace-id";
    
    @Value("${AUTH_CLIENT}")
    private String gatewayUri;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(new Server().url(gatewayUri + "/api/messaging")))
                .info(new Info().title("Messaging Service API").version("1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList(COOKIE_AUTH_NAME).addList(WORKSPACE_HEADER))
                .components(new Components()
                        .addSecuritySchemes(COOKIE_AUTH_NAME, new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.COOKIE)
                                .name("SESSION")
                                .description("Session cookie for authentication"))
                        .addSecuritySchemes(WORKSPACE_HEADER, new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name(WORKSPACE_HEADER)
                                .description("Workspace ID for multi-tenant requests")));
    }
}
