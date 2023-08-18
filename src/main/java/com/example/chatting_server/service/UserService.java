package com.example.chatting_server.service;

import com.example.chatting_server.vo.request.*;
import com.example.chatting_server.vo.response.ResponseVo;

public interface UserService {
    ResponseVo login(LoginVo loginVo);
    ResponseVo logout(String accessToken, String refreshToken);
    ResponseVo updateToken(String userId, String id, String refreshToken);
    ResponseVo getUserIdValidate(String userId);
    ResponseVo createUser(CreateUserVo createUserVo);
    ResponseVo getUserId(FindUserIdVo findUserIdVo);
    ResponseVo updatePassword(UpdatePasswordVo updatePasswordVo);
    ResponseVo getUser(String id);
    ResponseVo updateUser(UpdateUserVo updateUserVo, String userId);
    ResponseVo deleteUser(String id, String accessToken, String refreshToken);
    ResponseVo createMetadata(String id, CreateUpdateUserMetadataVo createUpdateUserMetadataVo);
    ResponseVo getMetadata(String id, String userId);
    ResponseVo updateMetadata(String id, CreateUpdateUserMetadataVo createUpdateUserMetadataVo);
    ResponseVo deleteMetadata(String id);
}
