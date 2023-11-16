package hyeon.buddy.service;

import hyeon.buddy.exception.CustomException;
import hyeon.buddy.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    private final static String KEY_REFRESH_TOKEN = "RT:";
    private final static String KEY_RECOMMEND = "RC:";
    private final static String KEY_FEEDBACK = "FB:";
    private final static Integer TTL_REFRESH_TOKEN = 60 * 60 * 24 * 10; // TTL 10일로 설정


    //  당일 예산 추천 정보 dto를 json 형태로 자정까지 redis에 저장
    /*@Transactional
    public void saveRecommendInfo(Long id, RecommendDayResponseDTO dto) {
        String key = KEY_RECOMMEND + id.toString(); // Use id in the key

        LocalDateTime midnight = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        long secondsUntilMidnight = LocalDateTime.now().until(midnight, ChronoUnit.SECONDS);

        try {
            // JSON string으로 형 변환
            String dtoAsString = new ObjectMapper().writeValueAsString(dto);

            redisTemplate.opsForValue().set(key, dtoAsString);
            redisTemplate.expire(key, secondsUntilMidnight, TimeUnit.SECONDS);

        } catch (Exception e) {
            log.info("userId(" + id + ")json 형변환을 통한 추천 정보 저장 실패");
            throw new CustomException(ExceptionCode.RECOMMEND_NOT_CREATED);
        }
    }*/

    // 당일 예산 추천 정보를 string 문자열 저장
    @Transactional
    public void saveRecommendInfo(Long id, String dto) {
        String key = KEY_RECOMMEND + id.toString();

        LocalDateTime midnight = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        long secondsUntilMidnight = LocalDateTime.now().until(midnight, ChronoUnit.SECONDS);

        try {
            redisTemplate.opsForValue().set(key, dto);
            redisTemplate.expire(key, secondsUntilMidnight, TimeUnit.SECONDS);

        } catch (Exception e) {
            log.info("userId(" + id + ")json 형변환을 통한 추천 정보 저장 실패");
            throw new CustomException(ExceptionCode.RECOMMEND_NOT_CREATED);
        }
    }

    // json 형태를 조회
    @Transactional
    public String getRecommendInfo(Long id) {
        String key = KEY_RECOMMEND + id.toString();
        String json = (String) redisTemplate.opsForValue().get(key);
        if (json == null) {
            return json;
        }

        try {
            /*ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, Object.class);*/
            return json;
        } catch (Exception e) {
            throw new CustomException(ExceptionCode.RECOMMEND_NOT_FOUND);
        }
    }

    /* token 처리 */

    @Transactional
    public void saveRefreshToken(Long id, String refreshToken) {
        String key = KEY_REFRESH_TOKEN + id.toString();

        redisTemplate.opsForValue().set(key, refreshToken);
        redisTemplate.expire(key, TTL_REFRESH_TOKEN, TimeUnit.MINUTES);
    }


    @Transactional
    public String getRefreshToken(Long id){
        String key = KEY_REFRESH_TOKEN + id.toString();

        return (String) redisTemplate.opsForValue().get(key);
    }

    @Transactional
    public void deleteRefreshToken(Long id){
        String key = KEY_REFRESH_TOKEN + id.toString();

        redisTemplate.delete(key);
    }
}