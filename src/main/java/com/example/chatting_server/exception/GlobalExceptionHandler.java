package com.example.chatting_server.exception;

import com.example.chatting_server.vo.response.ResponseVo;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static com.example.chatting_server.util.ResponseCode.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final String NOT_BLANK = "NotBlank";
    private final String NULL = "null";


    @ExceptionHandler(BindException.class)
    public ResponseEntity<ResponseVo> handleValidationException(BindException ex) {
        ResponseVo responseVo = null;

        List<FieldError> errorListlist = ex.getBindingResult().getFieldErrors();

        // TODO 여러개일 때 예외처리 필요
        FieldError fieldError = errorListlist.get(0);

        if (fieldError.getCode().equals(NOT_BLANK)) {
            responseVo = ResponseVo.builder()
                    .code(INVALID_PARAM_VALUE.getCode())
                    .message(INVALID_PARAM_VALUE.getMessage() + " (" + fieldError.getField() + ")")
                    .build();
        } else if (fieldError.getCode().equals(NULL)) {
            responseVo = ResponseVo.builder()
                    .code(NO_REQUIRED_PARAM.getCode())
                    .message(NO_REQUIRED_PARAM.getMessage() + " (" + fieldError.getField() + ")")
                    .build();
        }

        return ResponseEntity.badRequest().body(responseVo);
    }
}
