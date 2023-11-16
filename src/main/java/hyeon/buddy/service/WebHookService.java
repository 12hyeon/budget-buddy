package hyeon.buddy.service;

import hyeon.buddy.dto.RecommendDayDTO;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
public class WebHookService {

    @Value("${discord.webhookURL}")
    private  String url;

    /* 새로운 당일 예산 추천 전송 */
    public String callFeedbackEvent(String account, List<RecommendDayDTO> dto) {
        JSONObject json = new JSONObject();
        StringBuilder s = new StringBuilder(account + "님,\n\n");

        for (RecommendDayDTO recommendDayDTO : dto) {
            s.append("### ").append(recommendDayDTO.getCategory()).append("\n")
                    .append("- 이번 달 예산: **").append(recommendDayDTO.getMonthBudget()).append("원**\n")
                    .append("- 오늘의 추천 예산: **").append(recommendDayDTO.getTodayBudget()).append("원**\n\n");
        }

        try {
            json.put("content", s);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        send(json);
        return s.toString();
    }

    /* redis에 저장된 당일 예산 추천 전송 */
    public void callFeedbackEvent(String s) {
        JSONObject data = new JSONObject();

        try {
            data.put("content", s);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        send(data);
    }


    /* json으로 메시지 전송 */
    private void send(JSONObject json){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(json.toString(), headers);
        restTemplate.postForObject(url, entity, String.class);
    }

}
