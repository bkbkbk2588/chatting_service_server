package com.example.chatting_server.service;

import com.example.chatting_server.vo.response.ResponseVo;

public interface FriendService {
    ResponseVo postFriend(String id, String nickname);
    ResponseVo friendAccept(String id, String friendId);
    ResponseVo friendRefuse(String id, String friendId);
}
