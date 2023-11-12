package hyeon.buddy.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "예산 관리 어플리케이션 <Budget Buddy> REST API",
                description = "Spring Boot를 이용한 REST API 프로젝트",
                version = "0.0.1",
                contact = @io.swagger.v3.oas.annotations.info.Contact(
                        name = "12hyeon",
                        url = "https://github.com/12hyeon/budget-buddy",
                        email = "1212guswjd@gmail.com")
        )
)
public class SwaggerConfig { // http://localhost:8080/swagger-ui/index.html

    @Bean
    public GroupedOpenApi chatOpenApi() {
        String[] paths = {"/api/v1/**"};

        return GroupedOpenApi.builder()
                .group("Budget Buddy API v1")
                .pathsToMatch(paths)
                .build();
    }

    /* swagger-ui 페이지 연결 핸들러 설정 */
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry
                .addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}

