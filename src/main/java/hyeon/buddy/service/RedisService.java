package hyeon.buddy.service;

import hyeon.buddy.exception.CustomException;
import hyeon.buddy.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate stringRedisTemplate;

    private final static String KEY_REFRESH_TOKEN = "RT:";
    private final static Integer TTL_REFRESH_TOKEN = 60 * 60 * 24 * 10; // TTL 10일로 설정

    @Transactional
    public void saveRefreshToken(Long id, String refreshToken) {
        String key = KEY_REFRESH_TOKEN + id.toString();

        stringRedisTemplate.opsForValue().set(key, refreshToken);
        stringRedisTemplate.expire(key, TTL_REFRESH_TOKEN, TimeUnit.MINUTES);
    }

    @Transactional
    public String getRefreshToken(Long id){
        String key = KEY_REFRESH_TOKEN + id.toString();

        return stringRedisTemplate.opsForValue().get(key);
    }

    @Transactional
    public void checkRefreshToken(Long id) {
        String key = KEY_REFRESH_TOKEN + id.toString();

        Optional.ofNullable(stringRedisTemplate.opsForValue().get(key))
                .orElseThrow(() -> new CustomException(ExceptionCode.TOKEN_NOT_FOUND));
    }

    @Transactional
    public void deleteRefreshToken(Long id){
        String key = KEY_REFRESH_TOKEN + id.toString();

        stringRedisTemplate.delete(key);
    }
}