package com.example.chatting_server.service;

import com.example.chatting_server.vo.request.*;
import com.example.chatting_server.vo.response.ResponseVo;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface ChannelService {
    ResponseVo postChannel(String userPkId, PostChannelVo postChannelVo);
    ResponseVo updateChannel(String userPkId, UpdateChannel updateChannel) throws JsonProcessingException;
    ResponseVo deleteChannel(String userPkId, String channelUrl);
    ResponseVo leaveChannel(String userPkId, String channelUrl);
    ResponseVo updateHideChannel(String userPkId, UpdateHideChannelVo updateHideChannelVo);
    ResponseVo getChannelActiveUser(String userPkId, String channelUrl);
    ResponseVo getChannelInviteUser(String userPkId, String channelUrl);
    ResponseVo getInviteChannelList(String userPkId);
    ResponseVo inviteChannelUser(String userPkId, InviteChannelUserVo inviteChannelUserVo);
    ResponseVo updateInviteChannelUser(String userPkId, ChannelUserIdVo channelUserIdVo, int userState);
    ResponseVo postChannelMetadata(String userPkId, ChannelMetadataVo channelMetadataVo);
    ResponseVo updateChannelMetadata(String userPkId, ChannelMetadataVo channelMetadataVo);
}
