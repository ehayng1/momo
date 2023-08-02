package kr.co.dk.sample.api.config.openapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * https://springdoc.org/faq.html#how-can-i-map-pageable-spring-date-commons-object-to-correct-url-parameter-in-swagger-ui
 * https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Annotations#arrayschema
 */
@OpenAPIDefinition(
        servers = {
                @Server(url = "https://api.buildmomo.com", description = "서버"),
                @Server(url = "http://localhost:8088", description = "개발"), @Server(url = "http://localhost:8088", description = "로컬")
        },
        info = @Info(
                title = "momo api", version = "v1"
        ),
        security = @SecurityRequirement(
                name = "bearerAuth"
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "Bearer"
)
@Configuration
public class OpenApiConfig {

    @Bean
    GroupedOpenApi userApis() {
        return GroupedOpenApi.builder().group("user").pathsToMatch("/**").pathsToExclude("/**/**/admin/**").build();
    }

}
