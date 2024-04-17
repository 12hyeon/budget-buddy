package hyeon.buddy.config;

import hyeon.buddy.service.WebHookService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfiguration {

    @Bean
    public WebHookService webHookService(){
        return new WebHookService();
    }
}
