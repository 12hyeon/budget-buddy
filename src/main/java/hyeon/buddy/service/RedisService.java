package hyeon.buddy.service;

import hyeon.buddy.dto.RecommendResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, RecommendResponseDTO> recommendRedisTemplate;

    private final static String KEY_REFRESH_TOKEN = "RT:";
    private final static String KEY_RECOMMEND = "RC:";
    private final static Integer TTL_REFRESH_TOKEN = 60 * 60 * 24 * 10; // TTL 10일로 설정


    /* 당일 예산 추천 정보를 자정까지 저장 */

    @Transactional
    public void saveRecommendInfo(Long id, RecommendResponseDTO dto) {
        String key = KEY_RECOMMEND + id.toString();

        LocalDateTime midnight = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        long secondsUntilMidnight = LocalDateTime.now().until(midnight, ChronoUnit.SECONDS);

        recommendRedisTemplate.opsForValue().set(key, dto);
        stringRedisTemplate.expire(key, secondsUntilMidnight, TimeUnit.SECONDS);
    }

    @Transactional
    public RecommendResponseDTO getRecommendInfo(Long id) {
        String key = KEY_RECOMMEND + id.toString();
        return recommendRedisTemplate.opsForValue().get(key);
    }


    /* token 처리 */

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
    public void deleteRefreshToken(Long id){
        String key = KEY_REFRESH_TOKEN + id.toString();

        stringRedisTemplate.delete(key);
    }
}