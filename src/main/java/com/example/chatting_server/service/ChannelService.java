package com.example.chatting_server.service;

import com.example.chatting_server.vo.request.PostChannelVo;
import com.example.chatting_server.vo.response.ResponseVo;

public interface ChannelService {
    ResponseVo postChannel(String userPkId, PostChannelVo postChannelVo);
}
