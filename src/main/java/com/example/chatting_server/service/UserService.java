package com.example.chatting_server.service;

import com.example.chatting_server.vo.request.CreateUserVo;
import com.example.chatting_server.vo.request.FindUserIdVo;
import com.example.chatting_server.vo.request.LoginVo;
import com.example.chatting_server.vo.request.UpdatePasswordVo;
import com.example.chatting_server.vo.response.ResponseVo;

public interface UserService {
    ResponseVo login(LoginVo loginVo);
    ResponseVo logout(String accessToken, String refreshToken);
    ResponseVo updateToken(String userId, String refreshToken);
    ResponseVo getUserIdValidate(String userId);
    ResponseVo createUser(CreateUserVo createUserVo);
    ResponseVo getUserId(FindUserIdVo findUserIdVo);
    ResponseVo updatePassword(UpdatePasswordVo updatePasswordVo);
    ResponseVo getUser(String userId);
}
