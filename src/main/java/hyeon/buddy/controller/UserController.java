package hyeon.buddy.controller;

import hyeon.buddy.dto.UserSignInRequestDTO;
import hyeon.buddy.dto.UserSignInResponseDTO;
import hyeon.buddy.dto.UserSignUpRequestDTO;
import hyeon.buddy.exception.ExceptionResponse;
import hyeon.buddy.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Tag(name = "User API", description = "유저와 관련된 API")
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    @Operation(summary = "회원가입", description = "작성한 사용자 정보를 기반으로 가입을 진행합니다.")
    public ResponseEntity<ExceptionResponse> signUp(@RequestBody @Valid UserSignUpRequestDTO dto) {
        return ResponseEntity.status(OK).body(userService.signUp(dto));
    }

    @PostMapping("/sign-in")
    @Operation(summary = "로그인", description = "입력한 정보로 로그인을 진행합니다.")
    public ResponseEntity<UserSignInResponseDTO> signIn(@RequestBody @Valid UserSignInRequestDTO dto) {
        return ResponseEntity.status(OK).body(userService.signIn(dto));
    }

}