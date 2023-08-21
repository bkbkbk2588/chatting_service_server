package com.example.chatting_server.customRepository;

import com.example.chatting_server.vo.response.FriendUserVo;

import java.util.List;

public interface CustomFriendRepository {
    List<FriendUserVo> getFriendList(String id, int userStatus);
}
