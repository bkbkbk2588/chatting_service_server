package com.example.chatting_server.security.component;

import com.example.chatting_server.vo.response.ResponseVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.example.chatting_server.util.ResponseCode.UNAUTHORIZED;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        PrintWriter writer = response.getWriter();

        try {
            ResponseVo responseVo = ResponseVo.builder()
                    .code(UNAUTHORIZED.getCode())
                    .message(UNAUTHORIZED.getMessage())
                    .build();

            ObjectMapper objectMapper = new ObjectMapper();
            String responseBody = objectMapper.writeValueAsString(responseVo);

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            writer.write(responseBody);
        } catch (NullPointerException e) {
            logger.error("JwtAuthenticationEntryPoint", e);
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }
}
