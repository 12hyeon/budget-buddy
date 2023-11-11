package hyeon.buddy.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequestDTO {

    @NotBlank(message = "계정을 입력해 주세요.")
    private String account;

    @NotBlank(message = "이메일을 입력해 주세요.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @Size(min = 8, max = 16, message = "비밀번호는 8~16자 사이로 입력해 주세요.")
    private String password;

    public void setPassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
