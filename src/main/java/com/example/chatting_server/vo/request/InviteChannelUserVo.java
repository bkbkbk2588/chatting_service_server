package com.example.chatting_server.vo.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
public class InviteChannelUserVo {
    @NotNull
    @Size(min = 1, max = 50)
    private List<String> inviteNickNameList;

    @NotNull
    @NotBlank
    private String channelUrl;
}
