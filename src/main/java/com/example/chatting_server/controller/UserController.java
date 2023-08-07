package com.example.chatting_server.controller;

import com.example.chatting_server.service.UserService;
import com.example.chatting_server.vo.request.CreateUserVo;
import com.example.chatting_server.vo.response.ResponseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.example.chatting_server.util.ResponseCode.NO_REQUIRED_PARAM;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    final UserService userService;

    /**
     * * ID 중복확인
     */
    @GetMapping("/check-id")
    public ResponseVo getUserIdValidate(@RequestParam @NotNull @NotBlank String userId) {
        ResponseVo response = userService.getUserIdValidate(userId);

        return response;
    }

    /**
     * * 회원가입
     */
    @PostMapping("/sign-up")
    public ResponseVo createUser(@Valid @RequestBody CreateUserVo createUserVo) {
        ResponseVo response = userService.createUser(createUserVo);

        return response;
    }
}
