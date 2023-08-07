package com.example.chatting_server.service;

import com.example.chatting_server.vo.request.CreateUserVo;
import com.example.chatting_server.vo.response.ResponseVo;

public interface UserService {
    ResponseVo getUserIdValidate(String userId);
    ResponseVo createUser(CreateUserVo createUserVo);
}
