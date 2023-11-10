package hyeon.buddy.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Validation 예외
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidException(
            MethodArgumentNotValidException exception) {

        List<String> errors = new ArrayList<>();
        exception.getBindingResult().getAllErrors()
                .forEach(error -> errors.add(error.getDefaultMessage()));

        return ResponseEntity.status(400).body(errors);
    }

    // CustomException 예외
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionResponse> handleCustomException(CustomException exception) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .exceptionCode(exception.getExceptionCode())
                .message(exception.getMessage())
                .build();

        log.error(exception.getMessage(),exception);

        return ResponseEntity
                .status(exceptionResponse.getExceptionCode().getStatus())
                .body(exceptionResponse);
    }

    // 정의되지 않은 예외 발생
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> undefinedException(Exception exception) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .message(exception.getMessage())
                .exceptionCode(ExceptionCode.UNDEFINED_EXCEPTION)
                .build();

        log.error(exception.getMessage(),exception);

        return ResponseEntity
                .status(exceptionResponse.getExceptionCode().getStatus())
                .body(exceptionResponse);
    }
}