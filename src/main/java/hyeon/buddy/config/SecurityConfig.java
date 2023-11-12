package hyeon.buddy.config;

import hyeon.buddy.security.JwtAccessDeniedHandler;
import hyeon.buddy.security.JwtAuthenticationEntryPoint;
import hyeon.buddy.security.TokenAuthenticationFilter;
import hyeon.buddy.security.TokenProvider;
import hyeon.buddy.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final UserDetailsServiceImpl userDetailsService;

    private static final String[] DEFAULT_LIST = {
            "/swagger-ui", "/swagger-config", "/swagger-ui/index.html"
    };
    private static final String[] WHITE_LIST = {
            "/api/v1/users/**"
    };

    // Security 필터 체인 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .headers(c -> c.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable).disable())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(WHITE_LIST).permitAll()
                                .requestMatchers(DEFAULT_LIST).permitAll()
                                .requestMatchers( "/","/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .anyRequest().authenticated()
                )
                .addFilterBefore(tokenAuthenticationFilter(userDetailsService, new TokenProvider()), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }



    // 비밀번호 암호화에 사용
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 토큰 인증 및 유저 정보 추출에 사용
    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter(UserDetailsServiceImpl userDetailsService, TokenProvider tokenProvider) {
        return new TokenAuthenticationFilter(tokenProvider, userDetailsService);
    }
}
