package com.example.chatting_server.customRepository;

import com.example.chatting_server.entity.ChannelUser;
import com.example.chatting_server.vo.response.ChannelActiveUserVo;

import java.util.List;

public interface CustomChannelUserRepository {
    ChannelUser getChannelUser(String channelUrl, String id);
    List<ChannelActiveUserVo> getChannelUserStateList(String channelUrl, int userState);
    long checkIncludeChannelUser(String channelUrl, String id);
}
