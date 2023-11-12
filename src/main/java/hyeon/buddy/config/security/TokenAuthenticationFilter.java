package hyeon.buddy.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import hyeon.buddy.exception.ExceptionCode;
import hyeon.buddy.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String jwt = getJwtFromRequest(request);
        if (StringUtils.hasText(jwt)) {
            ExceptionCode validationCode = tokenProvider.validateToken(jwt);
            if (validationCode == ExceptionCode.TOKEN_SUCCESS) {

                // 유저 정보 추출
                UserDetails userDetails = userDetailsService.loadUserByUsername(tokenProvider.getIdFromToken(jwt));

                // 인증된 객체 생성
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                filterChain.doFilter(request, response);
            } else {
                createErrorResponse(validationCode, response);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    // jwt 토큰 추출
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 예외 처리 응답 생성
    private void createErrorResponse(ExceptionCode exceptionCode, HttpServletResponse response) throws IOException {
        ObjectNode json = new ObjectMapper().createObjectNode();
        json.put("state", HttpServletResponse.SC_UNAUTHORIZED);
        json.put("code", exceptionCode.getCode());
        json.put("message", exceptionCode.getMessage());

        String newResponse = new ObjectMapper().writeValueAsString(json);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setContentLength(newResponse.getBytes(StandardCharsets.UTF_8).length);
        response.getWriter().write(newResponse);
    }
}
