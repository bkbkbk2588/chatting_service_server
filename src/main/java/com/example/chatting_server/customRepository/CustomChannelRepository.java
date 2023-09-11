package com.example.chatting_server.customRepository;

import com.example.chatting_server.vo.request.ChannelListVo;
import com.example.chatting_server.vo.response.ChannelDetailVo;
import com.example.chatting_server.vo.response.ChannelInfoResultVo;

import java.util.List;

public interface CustomChannelRepository {
    List<ChannelInfoResultVo> getChannelList(String userPkId, ChannelListVo channelListVo);
    List<ChannelDetailVo> getChannelDetail(String userPkId, String channelUrl);
}
