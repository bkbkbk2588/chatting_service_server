package com.example.chatting_server.customRepository;

import com.example.chatting_server.vo.response.ChannelAndMetadataInfoVo;

public interface CustomChannelMetadataRepository {
    ChannelAndMetadataInfoVo getChannelMetadata(String channelUrl);
}
