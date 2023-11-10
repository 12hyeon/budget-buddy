package hyeon.buddy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] DEFAULT_LIST = {
            "**/swagger-ui.html"
    };

    private static final String[] WHITE_LIST = {
            "/api/v1/auth/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().disable()
                .authorizeRequests(auth -> {
                    try {
                        auth
                                .requestMatchers(WHITE_LIST).permitAll()
                                .requestMatchers(DEFAULT_LIST).permitAll()
                                .anyRequest().authenticated();
                    } catch (Exception e) {
                        // Handle exception if needed
                        e.printStackTrace();
                    }
                })
                .exceptionHandling()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }
}

