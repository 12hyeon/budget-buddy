package hyeon.buddy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignInRequestDTO {

    @NotBlank(message = "이메일을 입력해 주세요.")
    private String email;

    @Size(min = 8, max = 16, message = "비밀번호는 8~16자 사이로 입력해 주세요.")
    private String password;

    public UsernamePasswordAuthenticationToken toAuthentication(){
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
