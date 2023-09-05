package com.example.chatting_server.customRepository;

import com.example.chatting_server.vo.request.ChannelListVo;
import com.example.chatting_server.vo.response.ChannelInfoListVo;

import java.util.List;

public interface CustomChannelRepository {
    List<ChannelInfoListVo> getChannelList(ChannelListVo channelListVo);
}
