package com.example.chatting_server.controller;

import com.example.chatting_server.vo.request.CreateUserVo;
import com.example.chatting_server.vo.response.ResponseVo;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
    // TODO 1. 로그 한곳에서 관리 2. 응답 vo 설정

    @GetMapping("/test")
    public int test() {
        return 1;
    }

    @PostMapping("/sign-up")
    public ResponseVo createUser(@Valid @RequestBody CreateUserVo createUserVo) {

        System.out.println("@@@@@@@@@@@@@@");
        return null;
    }
}
