package com.example.chatting_server.vo.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class FriendUserListVo {
    private List<FriendUserVo> friendList;
}
