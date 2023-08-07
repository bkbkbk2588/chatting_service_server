package com.example.chatting_server.exception;

import com.example.chatting_server.vo.response.ResponseVo;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.RollbackException;
import java.util.List;

import static com.example.chatting_server.util.ResponseCode.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private final String NOT_BLANK = "NotBlank";
    private final String NULL = "NotNull";


    @ExceptionHandler(BindException.class)
    public ResponseEntity<ResponseVo> handleValidationException(BindException ex) {
        log.error("BindException Error");

        ResponseVo responseVo = null;

        FieldError fieldError = ex.getFieldError();

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

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ResponseVo> handleInvalidFormatException(InvalidFormatException ex) {
        log.error("InvalidFormatException Error");

        String filedName = "";

        List<JsonMappingException.Reference> filedList = ex.getPath();

        if (filedList != null && filedList.size() > 0) {
            filedName = " (" + filedList.get(0).getFieldName() + ")";
        }

        ResponseVo responseVo = ResponseVo.builder()
                .code(INVALID_PARAM_TYPE.getCode())
                .message(INVALID_PARAM_TYPE.getMessage() + filedName)
                .build();

        return ResponseEntity.badRequest().body(responseVo);
    }

    @ExceptionHandler(RollbackException.class)
    public ResponseEntity<ResponseVo> handleRollbackException(RollbackException ex) {
        log.error("RollbackException Error");

        ResponseVo responseVo = ResponseVo.builder()
                .code(DB_COMMIT_FAIL.getCode())
                .message(DB_COMMIT_FAIL.getMessage())
                .build();

        return ResponseEntity.badRequest().body(responseVo);
    }
}
