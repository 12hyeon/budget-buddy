package hyeon.buddy.controller;

import hyeon.buddy.dto.TokenResponseDTO;
import hyeon.buddy.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@Tag(name = "Auth API", description = "인증과 관련된 API")
@RequestMapping("/api/v1/auth")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;

    @PostMapping("/reissue")
    @Operation(summary = "RT 재발급", description = "새로운 RT 토큰을 발급하거나 재 로그인을 요청합니다.")
    public ResponseEntity<TokenResponseDTO> signIn(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(OK).body(userService.reissue(Long.valueOf(userDetails.getUsername())));
    }

}