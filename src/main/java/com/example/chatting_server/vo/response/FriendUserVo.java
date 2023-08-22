package com.example.chatting_server.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class FriendUserVo {
    private String nickName;
    private String friendId;
    private int userStatus;
    private LocalDateTime inviteTime;
}
