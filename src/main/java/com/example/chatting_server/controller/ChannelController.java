package com.example.chatting_server.controller;

import com.example.chatting_server.service.ChannelService;
import com.example.chatting_server.vo.request.*;
import com.example.chatting_server.vo.response.ResponseVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.chatting_server.util.ChatCode.INVITE_ACCEPT;
import static com.example.chatting_server.util.ChatCode.INVITE_REFUSE;

@RestController
@RequestMapping("/channel")
@RequiredArgsConstructor
public class ChannelController {
    final ChannelService channelService;

    /**
     * * 채널 생성(방장 권한)
     */
    @PostMapping
    public ResponseVo postChannel(Authentication authentication, @Valid @RequestBody PostChannelVo postChannelVo) {
        return channelService.postChannel(String.valueOf(authentication.getCredentials()), postChannelVo);
    }

    /**
     * * 채널 수정(방장 권한)
     */
    @PutMapping
    public ResponseVo updateChannel(Authentication authentication, @Valid @RequestBody UpdateChannel updateChannel) throws JsonProcessingException {
        return channelService.updateChannel(String.valueOf(authentication.getCredentials()), updateChannel);
    }

    /**
     * * 채널 삭제(방장 권한)
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

    /**
     * * 채널 숨기기/숨기기 해제
     */
    @PutMapping("/hide")
    public ResponseVo updateHideChannel(Authentication authentication, @Valid @RequestBody UpdateHideChannelVo updateHideChannelVo) {
        return channelService.updateHideChannel(String.valueOf(authentication.getCredentials()), updateHideChannelVo);
    }

    /**
     * * 채널에 참가 중인 사용자 조회
     */
    @GetMapping("/{channelUrl}/active/user")
    public ResponseVo getChannelActiveUser(Authentication authentication, @PathVariable String channelUrl) {
        return channelService.getChannelActiveUser(String.valueOf(authentication.getCredentials()), channelUrl);
    }

    /**
     * * 채널 초대 유저 상태 조회(방장 권한)
     */
    @GetMapping("/{channelUrl}/invite/list")
    public ResponseVo getChannelInviteUser(Authentication authentication, @PathVariable String channelUrl) {
        return channelService.getChannelInviteUser(String.valueOf(authentication.getCredentials()), channelUrl);
    }

    /**
     * * 초대 목록 조회
     */
    @GetMapping("/invite/list")
    public ResponseVo getInviteChannelList(Authentication authentication) {
        return channelService.getInviteChannelList(String.valueOf(authentication.getCredentials()));
    }

    /**
     * * 사용자 채널 초대
     */
    @PostMapping("/invite")
    public ResponseVo inviteChannelUser(Authentication authentication, @Valid @RequestBody InviteChannelUserVo inviteChannelUserVo) {
        return channelService.inviteChannelUser(String.valueOf(authentication.getCredentials()), inviteChannelUserVo);
    }

    /**
     * * 사용자 채널 초대 수락
     */
    @PutMapping("/invite/accept")
    public ResponseVo inviteAcceptChannelUser(Authentication authentication, @Valid @RequestBody ChannelUserIdVo channelUserIdVo) {
        return channelService.updateInviteChannelUser(String.valueOf(authentication.getCredentials()), channelUserIdVo, INVITE_ACCEPT.getCode());
    }

    /**
     * * 사용자 채널 초대 거절
     */
    @DeleteMapping("/invite/refuse")
    public ResponseVo inviteRefuseChannelUser(Authentication authentication, @Valid @RequestBody ChannelUserIdVo channelUserIdVo) {
        return channelService.updateInviteChannelUser(String.valueOf(authentication.getCredentials()), channelUserIdVo, INVITE_REFUSE.getCode());
    }

    /**
     * * 채널 메타 데이터 추가
     */
    @PostMapping("/metadata")
    public ResponseVo postChannelMetadata(Authentication authentication, @Valid @RequestBody ChannelMetadataVo channelMetadataVo) {
        return channelService.postChannelMetadata(String.valueOf(authentication.getCredentials()), channelMetadataVo);
    }

    /**
     * * 채널 메타 데이터 수정
     */
    @PutMapping("/metadata")
    public ResponseVo updateChannelMetadata(Authentication authentication, @Valid @RequestBody ChannelMetadataVo channelMetadataVo) {
        return channelService.updateChannelMetadata(String.valueOf(authentication.getCredentials()), channelMetadataVo);
    }
}
