package com.example.chatting_server.security.component;

import com.example.chatting_server.vo.response.ResponseVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.example.chatting_server.util.ResponseCode.*;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final Logger logger = LoggerFactory.getLogger(JwtAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        PrintWriter writer = response.getWriter();

        try {
            ResponseVo responseVo = ResponseVo.builder()
                    .code(FORBIDDEN.getCode())
                    .message(FORBIDDEN.getMessage())
                    .build();

            ObjectMapper objectMapper = new ObjectMapper();
            String responseBody = objectMapper.writeValueAsString(responseVo);

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.FORBIDDEN.value());

            writer.write(responseBody);
        } catch (NullPointerException e) {
            logger.error("JwtAccessDeniedHandler", e);
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }
}
