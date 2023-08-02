package com.example.chatting_server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    // TODO 1. 로그 한곳에서 관리 2. 응답 vo 설정

    @GetMapping("/test")
    public int test() {
        return 1;
    }
}
