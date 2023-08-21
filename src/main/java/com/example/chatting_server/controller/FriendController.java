package com.example.chatting_server.controller;

import com.example.chatting_server.service.FriendService;
import com.example.chatting_server.vo.request.FriendIdVo;
import com.example.chatting_server.vo.request.RequestFriendVo;
import com.example.chatting_server.vo.response.ResponseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/friend")
@RequiredArgsConstructor
public class FriendController {

    final FriendService friendService;

    /**
     * 친구 추가 요청
     */
    @PostMapping
    public ResponseVo postFriend(Authentication authentication, @Valid @RequestBody RequestFriendVo requestFriendVo) {

        return friendService.postFriend(String.valueOf(authentication.getCredentials()), requestFriendVo.getNickName());
    }

    /**
     * 친구 추가 요청 수락
     */
    @PutMapping("/accept")
    public ResponseVo friendAccept(Authentication authentication, @Valid @RequestBody FriendIdVo friendIdVo) {

        return friendService.friendAccept(String.valueOf(authentication.getCredentials()), friendIdVo.getFriendId());
    }

    /**
     * 친구 추가 거절
     */
    @DeleteMapping("/refuse")
    public ResponseVo friendRefuse(Authentication authentication, @Valid @RequestBody FriendIdVo friendIdVo) {

        return friendService.friendRefuse(String.valueOf(authentication.getCredentials()), friendIdVo.getFriendId());
    }
}
