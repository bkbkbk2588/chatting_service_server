package com.example.chatting_server.service;

import com.example.chatting_server.vo.request.*;
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
    ResponseVo updateUser(UpdateUserVo updateUserVo, String userId);
    ResponseVo deleteUser(String userId);
}
