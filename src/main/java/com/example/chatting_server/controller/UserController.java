package com.example.chatting_server.controller;

import com.example.chatting_server.service.UserService;
import com.example.chatting_server.vo.request.*;
import com.example.chatting_server.vo.response.ResponseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    final UserService userService;

    /**
     * * 로그인
     */
    @PostMapping("/login")
    public ResponseVo login(@Valid @RequestBody LoginVo loginVo) {

        return userService.login(loginVo);
    }

    /**
     * * 로그아웃
     */
    @DeleteMapping("/logout")
    public ResponseVo logout(@RequestHeader("Authorization") String accessToken, @RequestHeader("RefreshToken") String refreshToken) {
        return userService.logout(accessToken, refreshToken);
    }

    /**
     * * 토큰 재발급
     */
    @PostMapping("/refresh")
    public ResponseVo updateToken(Authentication authentication, @RequestHeader("RefreshToken") String refreshToken) {
        return userService.updateToken(authentication.getName(), refreshToken);
    }

    /**
     * * ID 중복확인
     */
    @GetMapping("/check-id")
    public ResponseVo getUserIdValidate(@RequestParam @NotNull @NotBlank String userId) {

        return userService.getUserIdValidate(userId);
    }

    /**
     * * 회원가입
     */
    @PostMapping("/sign-up")
    public ResponseVo createUser(@Valid @RequestBody CreateUserVo createUserVo) {

        return userService.createUser(createUserVo);
    }

    /**
     * * ID 찾기
     */
    @PostMapping("/id")
    public ResponseVo getUserId(@Valid @RequestBody FindUserIdVo findUserIdVo) {

        return userService.getUserId(findUserIdVo);
    }

    /**
     * * 비밀번호 수정(찾기)
     */
    @PutMapping("/pwd")
    public ResponseVo updatePassword(@Valid @RequestBody UpdatePasswordVo updatePasswordVo) {

        return userService.updatePassword(updatePasswordVo);
    }

    /**
     * * 내 정보 조회
     */
    @GetMapping
    public ResponseVo getUser(Authentication authentication) {
        return userService.getUser(authentication.getName());
    }

    /**
     * * 내 회원 정보 수정
     */
    @PutMapping
    public ResponseVo updateUser(@Valid @RequestBody UpdateUserVo updateUserVo, Authentication authentication) {

        return userService.updateUser(updateUserVo, authentication.getName());
    }

    /**
     * * 회원 탈퇴
     */
    @DeleteMapping
    public ResponseVo deleteUser(Authentication authentication) {

        return userService.deleteUser(authentication.getName());
    }
}
