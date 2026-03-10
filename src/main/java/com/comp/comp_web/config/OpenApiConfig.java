package com.comp.comp_web.config;

import com.comp.comp_web.global.constants.ApiConstants;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title(ApiConstants.API_TITLE)
                .description(ApiConstants.API_DESCRIPTION)
                .version(ApiConstants.API_VERSION)
                .contact(new Contact()
                    .name(ApiConstants.API_CONTACT_NAME)
                )
                .license(new License()
                    .name(ApiConstants.API_LICENSE_NAME)
                    .url(ApiConstants.API_LICENSE_URL)))
            .components(new Components()
                .addSecuritySchemes(ApiConstants.BEARER_AUTH_SCHEME,
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat(ApiConstants.BEARER_FORMAT)
                        .description(ApiConstants.BEARER_AUTH_DESCRIPTION)
                ));
    }
}
