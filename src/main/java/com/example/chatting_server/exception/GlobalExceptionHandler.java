package com.example.chatting_server.exception;

import com.example.chatting_server.vo.response.ResponseVo;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.RollbackException;
import java.util.List;
import java.util.Objects;

import static com.example.chatting_server.util.ResponseCode.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private final String NOT_BLANK = "NotBlank";
    private final String NULL = "NotNull";
    private final String PATTERN = "Pattern";
    private final String SIZE = "Size";

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ResponseVo> handleValidationException(BindException ex) {
        log.error("BindException Error : {}", ex.getMessage());

        ResponseVo responseVo = null;

        FieldError fieldError = ex.getFieldError();

        if (Objects.equals(fieldError.getCode(), NOT_BLANK) || Objects.equals(fieldError.getCode(), PATTERN)) {
            responseVo = ResponseVo.builder()
                    .code(INVALID_PARAM_VALUE.getCode())
                    .message(INVALID_PARAM_VALUE.getMessage() + " (" + fieldError.getField() + ")")
                    .build();
        } else if (Objects.equals(fieldError.getCode(), NULL)) {
            responseVo = ResponseVo.builder()
                    .code(NO_REQUIRED_PARAM.getCode())
                    .message(NO_REQUIRED_PARAM.getMessage() + " (" + fieldError.getField() + ")")
                    .build();
        } else if (Objects.equals(fieldError.getCode(), SIZE)) {
            responseVo = ResponseVo.builder()
                    .code(INVALID_PARAM_LENGTH.getCode())
                    .message(INVALID_PARAM_LENGTH.getMessage() + " (" + fieldError.getField() + ")")
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

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ResponseVo> handleRMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        log.error("MissingServletRequestParameterException Error");

        ResponseVo responseVo = ResponseVo.builder()
                .code(NO_REQUIRED_PARAM.getCode())
                .message(NO_REQUIRED_PARAM.getMessage() + " (" + ex.getParameterName() + ")")
                .build();

        return ResponseEntity.badRequest().body(responseVo);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseVo> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("DataIntegrityViolationException Error");

        ResponseVo responseVo = ResponseVo.builder()
                .code(DB_COMMIT_FAIL.getCode())
                .message(DB_COMMIT_FAIL.getMessage())
                .build();

        return ResponseEntity.badRequest().body(responseVo);
    }
}
