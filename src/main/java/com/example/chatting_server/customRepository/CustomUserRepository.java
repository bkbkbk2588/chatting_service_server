package com.example.chatting_server.customRepository;

import com.example.chatting_server.vo.response.FriendUserInfoVo;
import com.example.chatting_server.vo.response.UserInfoVo;

import java.util.List;

public interface CustomUserRepository {
    UserInfoVo findByUserInfo(String userId);
    List<FriendUserInfoVo> findByFriendUser(String userId, List<String> inviteUserIdList);
}
