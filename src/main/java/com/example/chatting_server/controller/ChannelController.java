package com.example.chatting_server.controller;

import com.example.chatting_server.service.ChannelService;
import com.example.chatting_server.vo.request.PostChannelVo;
import com.example.chatting_server.vo.response.ResponseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
