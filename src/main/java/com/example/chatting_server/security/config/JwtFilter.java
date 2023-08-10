package com.example.chatting_server.security.config;

import com.example.chatting_server.exception.TokenInvalidException;
import com.example.chatting_server.security.component.TokenProvider;
import com.example.chatting_server.util.ResponseCode;
import com.example.chatting_server.vo.response.ResponseVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.example.chatting_server.util.ResponseCode.INVALID_ACCESS_TOKEN;
import static com.example.chatting_server.util.ResponseCode.INVALID_REFRESH_TOKEN;

@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    private final String AUTHORIZATION_HEADER = "Authorization";
    private final String REFRESH_HEADER = "RefreshToken";
    private final String REFRESH_URL = "/user/refresh";
    private final String LOGOUT_URL = "/user/logout";

    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = request;
        String accessToken = resolveToken(httpServletRequest, AUTHORIZATION_HEADER);
        String refreshToken = resolveToken(httpServletRequest, REFRESH_HEADER);
        String requestURI = httpServletRequest.getRequestURI();

        try {
            switch (requestURI) {
                case REFRESH_URL:
                    if (!(StringUtils.hasText(refreshToken) && tokenProvider.validateAccessToken(refreshToken) && redisTemplate.opsForValue().get(refreshToken) == null)) {
                        throw new TokenInvalidException(ResponseCode.INVALID_REFRESH_TOKEN);
                    } else {
                        Authentication authentication = tokenProvider.getAuthentication(refreshToken);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                    break;
                case LOGOUT_URL:
                    if (!(StringUtils.hasText(refreshToken) && tokenProvider.validateAccessToken(refreshToken) && redisTemplate.opsForValue().get(refreshToken) == null)) {
                        throw new TokenInvalidException(INVALID_REFRESH_TOKEN);
                    }

                    if (!(StringUtils.hasText(accessToken) && tokenProvider.validateAccessToken(accessToken) && redisTemplate.opsForValue().get(accessToken) == null)) {
                        throw new TokenInvalidException(INVALID_ACCESS_TOKEN);
                    }

                    break;

                default:
                    // 토큰 유효 확인 && 토큰 블랙리스트 확인
                    if (StringUtils.hasText(accessToken) && tokenProvider.validateAccessToken(accessToken) && redisTemplate.opsForValue().get(accessToken) == null) {
                        Authentication authentication = tokenProvider.getAuthentication(accessToken);
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        logger.info("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
                    } else {
                        logger.info("유효한 JWT 토큰이 없습니다., uri: {}", requestURI);
                    }
                    break;
            }
            chain.doFilter(httpServletRequest, response);
        } catch (TokenInvalidException exception) {
            PrintWriter writer = response.getWriter();
            ResponseVo responseVo = ResponseVo.builder()
                    .code(exception.getResponseCode().getCode())
                    .message(exception.getResponseCode().getMessage())
                    .build();

            ObjectMapper objectMapper = new ObjectMapper();
            String responseBody = objectMapper.writeValueAsString(responseVo);

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            writer.write(responseBody);
        }
    }

    /**
     * 토큰 정보 추출
     */
    private String resolveToken(HttpServletRequest request, String headerName) {
        String bearerToken = request.getHeader(headerName);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
