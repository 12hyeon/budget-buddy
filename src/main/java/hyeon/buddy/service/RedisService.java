package hyeon.buddy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hyeon.buddy.dto.RecommendResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    //private final RedisTemplate<String, String> jsonStringTemplate;

    private final static String KEY_REFRESH_TOKEN = "RT:";
    private final static String KEY_RECOMMEND = "RC:";
    private final static Integer TTL_REFRESH_TOKEN = 60 * 60 * 24 * 10; // TTL 10일로 설정


    /* 당일 예산 추천 정보를 자정까지 저장 */

    // dto를 json 형태로 redis에 저장
    @Transactional
    public void saveRecommendInfo(Long id, RecommendResponseDTO dto) {
        String key = KEY_RECOMMEND + id.toString();

        LocalDateTime midnight = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        long secondsUntilMidnight = LocalDateTime.now().until(midnight, ChronoUnit.SECONDS);

        try {
            // JSON string으로 형 변환
            String dtoAsString = new ObjectMapper().writeValueAsString(dto);

            stringRedisTemplate.opsForValue().set(key, dtoAsString);
            stringRedisTemplate.expire(key, secondsUntilMidnight, TimeUnit.SECONDS);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public RecommendResponseDTO getRecommendInfo(Long id) {
        String key = KEY_RECOMMEND + id.toString();
        String dtoAsString = stringRedisTemplate.opsForValue().get(key);

        if (dtoAsString != null) {
            try { // RecommendResponseDTO로 변환
                return new ObjectMapper().readValue(dtoAsString, RecommendResponseDTO.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
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