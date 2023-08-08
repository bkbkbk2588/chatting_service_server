package com.example.chatting_server.security.config;

import com.example.chatting_server.security.component.JwtAccessDeniedHandler;
import com.example.chatting_server.security.component.JwtAuthenticationEntryPoint;
import com.example.chatting_server.security.component.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.NullRequestCache;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .csrf().disable()
                .cors().and()

                .formLogin().disable()
                .httpBasic().disable()

                /**401, 403 Exception 핸들링 */
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                /** 세션 비활성화 */
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)


                /** HttpServletRequest를 사용하는 요청들에 대한 접근 제한 설정*/
                .and()
                .authorizeRequests()
                .antMatchers("/user/check-id").permitAll()
                .antMatchers("/user/sign-up").permitAll()
                .antMatchers("/user/id").permitAll()
                .antMatchers("/user/pwd").permitAll()
                .antMatchers("/user/login").permitAll()
                .anyRequest().authenticated()

                /** JwtSecurityConfig 적용 */
                .and()
                .apply(new JwtSecurityConfig(tokenProvider))

                .and()
                .requestCache()
                .requestCache(new NullRequestCache())
                .and()
                .build();
    }
}
