package com.example.chatting_server.controller;

import com.example.chatting_server.service.ChannelService;
import com.example.chatting_server.vo.request.PostChannelVo;
import com.example.chatting_server.vo.request.UpdateChannel;
import com.example.chatting_server.vo.response.ResponseVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/channel")
@RequiredArgsConstructor
public class ChannelController {
    final ChannelService channelService;

    /**
     * * 채널 생성
     */
    @PostMapping
    public ResponseVo postChannel(Authentication authentication, @Valid @RequestBody PostChannelVo postChannelVo) {
        return channelService.postChannel(String.valueOf(authentication.getCredentials()), postChannelVo);
    }

    /**
     * * 채널 수정
     */
    @PutMapping
    public ResponseVo updateChannel(Authentication authentication, @Valid @RequestBody UpdateChannel updateChannel) throws JsonProcessingException {
        return channelService.updateChannel(String.valueOf(authentication.getCredentials()), updateChannel);
    }

    /**
     * * 채널 삭제
     */
    @DeleteMapping("/{channelUrl}")
    public ResponseVo deleteChannel(Authentication authentication, @PathVariable String channelUrl) {
        return channelService.deleteChannel(String.valueOf(authentication.getCredentials()), channelUrl);
    }

    /**
     * * 채널 나가기
     */
    @DeleteMapping("/{channelUrl}/leave")
    public ResponseVo leaveChannel(Authentication authentication, @PathVariable String channelUrl) {
        return channelService.leaveChannel(String.valueOf(authentication.getCredentials()), channelUrl);
    }
}
