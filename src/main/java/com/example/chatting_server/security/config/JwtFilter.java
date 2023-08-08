package com.example.chatting_server.security.config;

import com.example.chatting_server.security.component.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    private final String AUTHORIZATION_HEADER = "Authorization";
    private final String REFRESH_HEADER = "RefreshToken";
    private final String REFRESH_URL = "/user/refresh";
    private final String LOGOUT_URL = "/user/logout";

    private final TokenProvider tokenProvider;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = request;
        String accessToken = resolveToken(httpServletRequest, AUTHORIZATION_HEADER);
        String refreshToken = resolveToken(httpServletRequest, REFRESH_HEADER);
        String requestURI = httpServletRequest.getRequestURI();

        switch (requestURI) {
            case REFRESH_URL:
            case LOGOUT_URL:
                tokenProvider.validateRefreshToken(refreshToken);
                // TODO access token 재발급 가능
                break;

            default:
                if (StringUtils.hasText(accessToken) && tokenProvider.validateAccessToken(accessToken)) {
                    Authentication authentication = tokenProvider.getAuthentication(accessToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    logger.info("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
                } else {
                    logger.info("유효한 JWT 토큰이 없습니다., uri: {}", requestURI);
                }
                break;
        }
        chain.doFilter(httpServletRequest, response);
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
