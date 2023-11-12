package hyeon.buddy.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SwaggerConfig implements WebMvcConfigurer {

    @Bean
    public GroupedOpenApi chatOpenApi() {
        String[] paths = {"/api/v1/**"};

        return GroupedOpenApi.builder()
                .group("Budget Buddy API v1")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public Info chatOpenApiInfo() {
        return new Info()
                 .title("예산 관리 어플리케이션 <Budget Buddy> REST API")
                 .contact(new Contact()
                         .name("12hyeon")
                         .url("https://github.com/12hyeon/budget-buddy")
                         .email("1212guswjd@gmail.com")
                 )
                 .version("1.0.0")
                 .description("Spring Boot를 이용한 REST API 프로젝트");
    }

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(chatOpenApiInfo())
                .components(components())
                .addSecurityItem(securityRequirement());
    }

    private Components components(){
        return new Components()
                .addSecuritySchemes("Bearer Authorization",
                        new SecurityScheme()
                                .name("Bearer 인증")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                );
    }

    private SecurityRequirement securityRequirement() {
        return new SecurityRequirement()
                .addList("Bearer Authorization");
    }
}
