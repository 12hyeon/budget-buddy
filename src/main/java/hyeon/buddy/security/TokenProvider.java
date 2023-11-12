package hyeon.buddy.security;

import hyeon.buddy.exception.ExceptionCode;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@PropertySource("classpath:application.yml")
@Service
public class TokenProvider {

    @Value("${auth.tokenSecret}")
    private String SECRET;

    @Value("${auth.tokenExpirationDay}")
    private Integer EXPIRATION;

    // refresh 여부에 따라 알맞은 AT 혹은 RT 토큰 발급
    public String createToken(Long id, boolean refresh) {
        int time = refresh ? EXPIRATION * 3 * 60 * 24 : EXPIRATION * 60 * 24; // RT : 20일, AT : 1일

        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .claim("id", id)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusDays(time).toInstant()))
                .signWith( SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    // 토큰에서 id를 추출
    public String getIdFromToken(String token) {

        Claims claims = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();

        Long id = claims.get("id", Long.class);

        return String.valueOf(id);
    }

    // 유효한 토큰인지 확인
    public ExceptionCode validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(authToken);
            return ExceptionCode.TOKEN_SUCCESS;
        } catch (MalformedJwtException ex) {
            log.info("토큰 오류 : Invalid JWT token");
            return ExceptionCode.TOKEN_INVALID;
        } catch (ExpiredJwtException ex) {
            log.info("토큰 오류 : Expired JWT token");
            return ExceptionCode.TOKEN_EXPIRED;
        } catch (UnsupportedJwtException ex) {
            log.info("토큰 오류 : Unsupported JWT token");
            return ExceptionCode.TOKEN_UNSUPPORTED;
        } catch (IllegalArgumentException ex) {
            log.info("토큰 오류 : JWT claims string is empty.");
            return ExceptionCode.TOKEN_EMPTY_CLAIMS_STRING;
        } catch (Exception ex) {
            log.info("토큰 오류 : Invalid JWT signature");
            return ExceptionCode.TOKEN_INVALID_SIGNATURE;
        }
    }

    // 토큰 만료 여부 확인
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();

            Date expirationDate = claims.getExpiration();
            return expirationDate.before(new Date());
        } catch (ExpiredJwtException ex) {
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

}
