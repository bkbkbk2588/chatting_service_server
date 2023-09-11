package com.example.chatting_server.vo.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelListVo {
    private int page;
    private int size;
    private String createdAfter;
    private String createBefore;
    private String channelName;
}
