package com.example.chatting_server.controller;

import com.example.chatting_server.service.UserService;
import com.example.chatting_server.vo.request.CreateUserVo;
import com.example.chatting_server.vo.request.FindUserIdVo;
import com.example.chatting_server.vo.request.LoginVo;
import com.example.chatting_server.vo.request.UpdatePasswordVo;
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
     * * 로그아웃
     */
    @DeleteMapping("/refresh")
    public ResponseVo updateToken(Authentication authentication) {
        return userService.updateToken(authentication.getName());
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

    @GetMapping
    public ResponseVo getUser() {
        return null;

    }
}
