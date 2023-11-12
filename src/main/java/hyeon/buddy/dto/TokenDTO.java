package hyeon.buddy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TokenDTO {

    private Long userId;
    private String accessToken;
    private String refreshToken;

}
