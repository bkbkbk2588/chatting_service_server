package com.example.chatting_server.customRepository;

import com.example.chatting_server.vo.response.UserInfoVo;

public interface CustomUserRepository {
    UserInfoVo findByUserInfo(String userId);
}
