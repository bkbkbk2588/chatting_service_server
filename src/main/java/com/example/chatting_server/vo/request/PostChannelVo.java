package com.example.chatting_server.vo.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
public class PostChannelVo {

    @NotNull
    @Size(min = 1, max = 50)
    private List<String> inviteNickNameList;

    private String metaData;
    private String channelName;
}
