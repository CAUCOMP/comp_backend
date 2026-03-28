package com.comp.comp_web.config;

import com.comp.comp_web.global.constants.ApiConstants;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement; // 이 import가 추가되어야 합니다!
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        // 1. 모든 API 요청에 적용할 보안 요구사항 정의
        SecurityRequirement securityRequirement = new SecurityRequirement()
            .addList(ApiConstants.BEARER_AUTH_SCHEME);

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
            .addSecurityItem(securityRequirement) // ⭐ 이 한 줄이 핵심입니다!
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
