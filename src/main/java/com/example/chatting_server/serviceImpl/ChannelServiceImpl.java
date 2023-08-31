package com.example.chatting_server.serviceImpl;

import com.example.chatting_server.entity.Channel;
import com.example.chatting_server.entity.ChannelMetaData;
import com.example.chatting_server.entity.ChannelUser;
import com.example.chatting_server.entity.User;
import com.example.chatting_server.repository.ChannelMetaDataRepository;
import com.example.chatting_server.repository.ChannelRepository;
import com.example.chatting_server.repository.ChannelUserRepository;
import com.example.chatting_server.repository.UserRepository;
import com.example.chatting_server.service.ChannelService;
import com.example.chatting_server.vo.request.*;
import com.example.chatting_server.vo.response.ChannelActiveUserVo;
import com.example.chatting_server.vo.response.FriendUserInfoVo;
import com.example.chatting_server.vo.response.InviteChannelVo;
import com.example.chatting_server.vo.response.ResponseVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.chatting_server.util.ChatCode.*;
import static com.example.chatting_server.util.ResponseCode.*;

@Service
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {
    final ChannelRepository channelRepository;

    final ChannelUserRepository channelUserRepository;

    final ChannelMetaDataRepository channelMetaDataRepository;

    final UserRepository userRepository;

    private final EntityManager entityManager;

    private final String CHANNEL_NAME = "Channel Name";
    private final String CHANNEL_URL = "ChannelURL";

    @Transactional
    @Override
    public ResponseVo postChannel(String userPkId, PostChannelVo postChannelVo) {
        ResponseVo response;
        List<String> inviteNickNameList = postChannelVo.getInviteNickNameList();

        Optional<List<User>> inviteUserList = userRepository.findByNickNameInAndUserStatus(inviteNickNameList, USER_OK.getCode());

        if (inviteUserList.isPresent()) {
            // 초대한 유저 중 1명이라도 없을 경우
            if (inviteUserList.get().size() != inviteNickNameList.size()) {
                response = ResponseVo.builder()
                        .code(NO_EXIST_USER.getCode())
                        .message(NO_EXIST_USER.getMessage())
                        .build();
            } else {
                List<String> inviteUserIdList = inviteUserList.get()
                        .stream()
                        .map(User::getId)
                        .collect(Collectors.toList());
                List<FriendUserInfoVo> friendUserInfoList = userRepository.findByFriendUser(userPkId, inviteUserIdList);

                // 친구 목록에 없는 유저가 있을 경우
                if (friendUserInfoList.size() != inviteNickNameList.size()) {
                    response = ResponseVo.builder()
                            .code(NO_EXIST_FRIEND.getCode())
                            .message(NO_EXIST_FRIEND.getMessage())
                            .build();
                } else { // 모든 유저가 존재/친구일 경우(채널/채널 유저/채널 메타데이터 insert)
                    Channel channel = channelRepository.save(Channel.builder()
                            .channelName(!emptyAndNullCheck(postChannelVo.getChannelName()) ? CHANNEL_NAME : postChannelVo.getChannelName())
                            .owner(User.builder()
                                    .id(userPkId)
                                    .build())
                            .createTime(LocalDateTime.now())
                            .build());

                    List<ChannelUser> channelUserList = new ArrayList<>();

                    // 방장 정보 추가
                    channelUserList.add(ChannelUser.builder()
                            .channel(channel)
                            .user(User.builder()
                                    .id(userPkId)
                                    .build())
                            .userState(INVITE_ACCEPT.getCode())
                            .hideState(CHANNEL_UNHIDE.getCode())
                            .build());

                    // 초대 받은 사람 추가
                    for (FriendUserInfoVo friendUserInfo : friendUserInfoList) {
                        channelUserList.add(ChannelUser.builder()
                                .channel(channel)
                                .user(User.builder()
                                        .id(friendUserInfo.getFriendId())
                                        .build())
                                .userState(INVITE_WAIT.getCode())
                                .hideState(CHANNEL_UNHIDE.getCode())
                                .build());
                    }

                    batchInsertEntities(channelUserList);

                    if (emptyAndNullCheck(postChannelVo.getMetaData())) {
                        channelMetaDataRepository.save(ChannelMetaData.builder()
                                .channel(channel)
                                .metadata(postChannelVo.getMetaData())
                                .build());
                    }


                    Map<String, String> dataMap = new HashMap<>();

                    dataMap.put(CHANNEL_URL, channel.getChannelUrl());

                    response = ResponseVo.builder()
                            .code(SUCCESS.getCode())
                            .message(SUCCESS.getMessage())
                            .data(dataMap)
                            .build();
                }
            }
        } else {
            response = ResponseVo.builder()
                    .code(NO_EXIST_USER.getCode())
                    .message(NO_EXIST_USER.getMessage())
                    .build();
        }

        return response;
    }

    @Transactional
    @Override
    public ResponseVo updateChannel(String userPkId, UpdateChannel updateChannel) {
        ResponseVo response;
        Optional<Channel> channel = channelRepository.findById(updateChannel.getChannelUrl());

        try {
            if (channel.isPresent()) {
                Channel channelEntity = channel.get();

                if (channelEntity.getOwner().getId().equals(userPkId)) {
                    Channel resultChannel = channelEntity;

                    // 채널 명이 변경이 있을 경우
                    if (emptyAndNullCheck(updateChannel.getChannelName())) {
                        resultChannel = channelRepository.save(Channel.builder()
                                .channelUrl(channelEntity.getChannelUrl())
                                .channelName(updateChannel.getChannelName())
                                .createTime(channelEntity.getCreateTime())
                                .owner(User.builder()
                                        .id(userPkId)
                                        .build())
                                .lastMessage(channelEntity.getLastMessage())
                                .lastMessageTime(channelEntity.getLastMessageTime())
                                .build());
                    }

                    // 메타 데이터 있을 경우
                    if (updateChannel.getMetadata() != null) {
                        Optional<ChannelMetaData> channelMetaData = channelMetaDataRepository.findByChannelChannelUrl(updateChannel.getChannelUrl());

                        ObjectMapper objectMapper = new ObjectMapper();
                        String metadata = objectMapper.writeValueAsString(updateChannel.getMetadata());

                        if (channelMetaData.isPresent()) { // 메타 데이터 수정
                            channelMetaDataRepository.save(ChannelMetaData.builder()
                                    .id(channelMetaData.get().getId())
                                    .channel(resultChannel)
                                    .metadata(metadata)
                                    .build());
                        } else { // 메타 데이터 추가
                            channelMetaDataRepository.save(ChannelMetaData.builder()
                                    .channel(resultChannel)
                                    .metadata(metadata)
                                    .build());
                        }
                    }

                    response = ResponseVo.builder()
                            .code(SUCCESS.getCode())
                            .message(SUCCESS.getMessage())
                            .build();

                } else {
                    response = ResponseVo.builder()
                            .code(UNAUTHORIZED_CHANNEL.getCode())
                            .message(UNAUTHORIZED_CHANNEL.getMessage())
                            .build();
                }
            } else {
                response = ResponseVo.builder()
                        .code(NO_EXIST_CHANNEL.getCode())
                        .message(NO_EXIST_CHANNEL.getMessage())
                        .build();
            }


        } catch (JsonProcessingException exception) {
            response = ResponseVo.builder()
                    .code(JSON_PARSE_ERROR.getCode())
                    .message(JSON_PARSE_ERROR.getMessage())
                    .build();
        }

        return response;
    }

    @Transactional
    @Override
    public ResponseVo deleteChannel(String userPkId, String channelUrl) {
        ResponseVo response;
        Optional<Channel> channel = channelRepository.findById(channelUrl);

        if (channel.isPresent()) {
            if (channel.get().getOwner().getId().equals(userPkId)) {
                Optional<List<ChannelUser>> channelUserList = channelUserRepository.findByChannelChannelUrl(channelUrl);
                channelUserList.ifPresent(channelUserRepository::deleteAll);

                Optional<ChannelMetaData> channelMetaData = channelMetaDataRepository.findByChannelChannelUrl(channelUrl);

                channelMetaData.ifPresent(channelMetaDataRepository::delete);

                channelRepository.deleteById(channelUrl);

                response = ResponseVo.builder()
                        .code(SUCCESS.getCode())
                        .message(SUCCESS.getMessage())
                        .build();
            } else {
                response = ResponseVo.builder()
                        .code(UNAUTHORIZED_CHANNEL.getCode())
                        .message(UNAUTHORIZED_CHANNEL.getMessage())
                        .build();
            }
        } else {
            response = ResponseVo.builder()
                    .code(NO_EXIST_CHANNEL.getCode())
                    .message(NO_EXIST_CHANNEL.getMessage())
                    .build();
        }
        return response;
    }

    @Transactional
    @Override
    public ResponseVo leaveChannel(String userPkId, String channelUrl) {
        ResponseVo response;
        Optional<Channel> channel = channelRepository.findById(channelUrl);

        if (channel.isPresent()) {
            Channel channelEntity = channel.get();

            // 방장이 나갈 경우
            if (channelEntity.getOwner().getId().equals(userPkId)) {
                Optional<List<ChannelUser>> channelUserList = channelUserRepository.findByChannelChannelUrl(channelUrl);

                if (channelUserList.isPresent()) {
                    if (channelUserList.get().size() == 1) { // 혼자 있는 채널을 나갈 경우 채널 삭제
                        channelUserList.ifPresent(channelUserRepository::deleteAll);

                        Optional<ChannelMetaData> channelMetaData = channelMetaDataRepository.findByChannelChannelUrl(channelUrl);
                        channelMetaData.ifPresent(channelMetaDataRepository::delete);

                        channelRepository.deleteById(channelUrl);
                    } else { // 방장 위임 및 channelUser 삭제
                        ChannelUser channelOwner = null;

                        for (ChannelUser channelUser : channelUserList.get()) {
                            if (!channelUser.getUser().getId().equals(userPkId)) {
                                channelOwner = channelUser;

                                break;
                            }
                        }

                        Optional<ChannelUser> deleteChannelUser = channelUserRepository.findByChannelChannelUrlAndUserId(channelUrl, userPkId);

                        if (deleteChannelUser.isPresent() && channelOwner != null) {
                            channelUserRepository.delete(deleteChannelUser.get());

                            channelRepository.save(Channel.builder()
                                    .channelUrl(channelEntity.getChannelUrl())
                                    .channelName(channelEntity.getChannelName())
                                    .createTime(channelEntity.getCreateTime())
                                    .owner(User.builder()
                                            .id(channelOwner.getUser().getId())
                                            .build())
                                    .lastMessage(channelEntity.getLastMessage())
                                    .lastMessageTime(channelEntity.getLastMessageTime())
                                    .build());
                        } else {
                            return ResponseVo.builder()
                                    .code(NO_EXIST_CHANNEL_USER.getCode())
                                    .message(NO_EXIST_CHANNEL_USER.getMessage())
                                    .build();
                        }
                    }
                }

            } else { // 일반 유저가 나갈 경우
                Optional<ChannelUser> deleteChannelUser = channelUserRepository.findByChannelChannelUrlAndUserId(channelUrl, userPkId);

                if (deleteChannelUser.isPresent()) {
                    channelUserRepository.delete(deleteChannelUser.get());
                } else {
                    return ResponseVo.builder()
                            .code(NO_EXIST_CHANNEL_USER.getCode())
                            .message(NO_EXIST_CHANNEL_USER.getMessage())
                            .build();
                }

            }

            response = ResponseVo.builder()
                    .code(SUCCESS.getCode())
                    .message(SUCCESS.getMessage())
                    .build();
        } else {
            response = ResponseVo.builder()
                    .code(NO_EXIST_CHANNEL.getCode())
                    .message(NO_EXIST_CHANNEL.getMessage())
                    .build();
        }
        return response;
    }

    @Transactional
    @Override
    public ResponseVo updateHideChannel(String userPkId, UpdateHideChannelVo updateHideChannelVo) {
        ResponseVo response;
        ChannelUser channelUser = channelUserRepository.getChannelUser(updateHideChannelVo.getChannelUrl(), userPkId);

        if (channelUser != null) {
            channelUserRepository.save(ChannelUser.builder()
                    .id(channelUser.getId())
                    .channel(channelUser.getChannel())
                    .user(channelUser.getUser())
                    .userState(channelUser.getUserState())
                    .hideState(updateHideChannelVo.getHide())
                    .build());

            response = ResponseVo.builder()
                    .code(SUCCESS.getCode())
                    .message(SUCCESS.getMessage())
                    .build();
        } else {
            response = ResponseVo.builder()
                    .code(NO_EXIST_CHANNEL.getCode())
                    .message(NO_EXIST_CHANNEL.getMessage())
                    .build();
        }
        return response;
    }

    @Override
    public ResponseVo getChannelActiveUser(String userPkId, String channelUrl) {
        ResponseVo response;
        long existUserCount = channelUserRepository.checkIncludeChannelUser(channelUrl, userPkId);

        if (existUserCount > 0L) {
            List<ChannelActiveUserVo> channelUserList = channelUserRepository.getChannelUserStateList(channelUrl, INVITE_ACCEPT.getCode());

            if (channelUserList != null && channelUserList.size() > 0) {
                Map<String, Object> dataMap = new HashMap<>();

                dataMap.put("channelActiveUser", channelUserList);

                response = ResponseVo.builder()
                        .code(SUCCESS.getCode())
                        .message(SUCCESS.getMessage())
                        .data(dataMap)
                        .build();
            } else {
                response = ResponseVo.builder()
                        .code(NO_EXIST_CHANNEL.getCode())
                        .message(NO_EXIST_CHANNEL.getMessage())
                        .build();
            }

        } else {
            response = ResponseVo.builder()
                    .code(NO_EXIST_CHANNEL.getCode())
                    .message(NO_EXIST_CHANNEL.getMessage())
                    .build();
        }

        return response;
    }

    @Override
    public ResponseVo getChannelInviteUser(String userPkId, String channelUrl) {
        ResponseVo response;
        Optional<Channel> channel = channelRepository.findById(channelUrl);

        if (channel.isPresent()) {
            Channel channelEntity = channel.get();

            if (channelEntity.getOwner().getId().equals(userPkId)) {
                List<ChannelActiveUserVo> channelUserList = channelUserRepository.getChannelUserStateList(channelUrl, INVITE_WAIT.getCode());

                if (channelUserList != null && channelUserList.size() > 0) {
                    Map<String, Object> dataMap = new HashMap<>();

                    dataMap.put("channelInviteUser", channelUserList);

                    response = ResponseVo.builder()
                            .code(SUCCESS.getCode())
                            .message(SUCCESS.getMessage())
                            .data(dataMap)
                            .build();
                } else {
                    response = ResponseVo.builder()
                            .code(NO_EXIST_INVITE_CHANNEL_USER.getCode())
                            .message(NO_EXIST_INVITE_CHANNEL_USER.getMessage())
                            .build();
                }
            } else {
                response = ResponseVo.builder()
                        .code(UNAUTHORIZED_CHANNEL.getCode())
                        .message(UNAUTHORIZED_CHANNEL.getMessage())
                        .build();
            }
        } else {
            response = ResponseVo.builder()
                    .code(NO_EXIST_CHANNEL.getCode())
                    .message(NO_EXIST_CHANNEL.getMessage())
                    .build();
        }
        return response;
    }

    @Override
    public ResponseVo getInviteChannelList(String userPkId) {
        List<InviteChannelVo> channelUserList = channelUserRepository.getInviteChannelList(userPkId, INVITE_WAIT.getCode());
        ResponseVo response;

        if (channelUserList != null && channelUserList.size() > 0) {
            Map<String, Object> dataMap = new HashMap<>();

            dataMap.put("inviteChannelList", channelUserList);

            response = ResponseVo.builder()
                    .code(SUCCESS.getCode())
                    .message(SUCCESS.getMessage())
                    .data(dataMap)
                    .build();
        } else {
            response = ResponseVo.builder()
                    .code(NO_EXIST_INVITE_CHANNEL.getCode())
                    .message(NO_EXIST_INVITE_CHANNEL.getMessage())
                    .build();
        }
        return response;
    }

    @Transactional
    @Override
    public ResponseVo inviteChannelUser(String userPkId, InviteChannelUserVo inviteChannelUserVo) {
        ResponseVo response;
        Optional<Channel> channel = channelRepository.findById(inviteChannelUserVo.getChannelUrl());

        if (channel.isPresent()) {
            Channel channelEntity = channel.get();

            // 방장일 경우
            if (channelEntity.getOwner().getId().equals(userPkId)) {

                Optional<List<User>> inviteUserList = userRepository.findByNickNameInAndUserStatus(inviteChannelUserVo.getInviteNickNameList(), USER_OK.getCode());

                // 유저가 있을 경우
                if (inviteUserList.isPresent()) {
                    if (inviteUserList.get().size() != inviteChannelUserVo.getInviteNickNameList().size()) {
                        response = ResponseVo.builder()
                                .code(NO_EXIST_USER.getCode())
                                .message(NO_EXIST_USER.getMessage())
                                .build();
                    } else {
                        List<String> inviteUserIdList = inviteUserList.get()
                                .stream()
                                .map(User::getId)
                                .collect(Collectors.toList());
                        List<FriendUserInfoVo> friendUserInfoList = userRepository.findByFriendUser(userPkId, inviteUserIdList);

                        // 친구 목록에 없는 유저가 있을 경우
                        if (friendUserInfoList.size() != inviteChannelUserVo.getInviteNickNameList().size()) {
                            response = ResponseVo.builder()
                                    .code(NO_EXIST_FRIEND.getCode())
                                    .message(NO_EXIST_FRIEND.getMessage())
                                    .build();
                        } else { // 친구 목록에 초대한 유저가 다 있을 경우
                            long channelInviteUserCount = channelUserRepository.getChannelInviteUser(inviteUserIdList, inviteChannelUserVo.getChannelUrl());

                            // 이미 초대 되어 있는 경우
                            if (channelInviteUserCount > 0L && channelInviteUserCount == inviteUserIdList.size()) {
                                response = ResponseVo.builder()
                                        .code(EXIST_INVITE_CHANNEL_USER.getCode())
                                        .message(EXIST_INVITE_CHANNEL_USER.getMessage())
                                        .build();
                            } else {
                                List<ChannelUser> channelUserList = new ArrayList<>();

                                // 초대 받은 사람 추가
                                for (FriendUserInfoVo friendUserInfo : friendUserInfoList) {
                                    channelUserList.add(ChannelUser.builder()
                                            .channel(channelEntity)
                                            .user(User.builder()
                                                    .id(friendUserInfo.getFriendId())
                                                    .build())
                                            .userState(INVITE_WAIT.getCode())
                                            .hideState(CHANNEL_UNHIDE.getCode())
                                            .build());
                                }

                                batchInsertEntities(channelUserList);

                                response = ResponseVo.builder()
                                        .code(SUCCESS.getCode())
                                        .message(SUCCESS.getMessage())
                                        .build();
                            }
                        }
                    }
                } else {
                    response = ResponseVo.builder()
                            .code(NO_EXIST_USER.getCode())
                            .message(NO_EXIST_USER.getMessage())
                            .build();
                }
            } else {
                response = ResponseVo.builder()
                        .code(UNAUTHORIZED_CHANNEL.getCode())
                        .message(UNAUTHORIZED_CHANNEL.getMessage())
                        .build();
            }
        } else {
            response = ResponseVo.builder()
                    .code(NO_EXIST_CHANNEL.getCode())
                    .message(NO_EXIST_CHANNEL.getMessage())
                    .build();
        }

        return response;
    }

    @Transactional
    @Override
    public ResponseVo updateInviteChannelUser(String userPkId, ChannelUserIdVo channelUserIdVo, int userState) {
        Optional<ChannelUser> channelUser = channelUserRepository.findById(channelUserIdVo.getChannelUserId());
        ResponseVo response;

        if (channelUser.isPresent()) {
            ChannelUser channelUserEntity = channelUser.get();

            if (channelUserEntity.getUser().getId().equals(userPkId) && channelUserEntity.getUserState() == INVITE_WAIT.getCode()) {
                if (userState == INVITE_ACCEPT.getCode()) {
                    channelUserRepository.save(ChannelUser.builder()
                            .id(channelUserEntity.getId())
                            .channel(channelUserEntity.getChannel())
                            .user(channelUserEntity.getUser())
                            .userState(INVITE_ACCEPT.getCode())
                            .hideState(channelUserEntity.getHideState())
                            .build());
                } else if (userState == INVITE_REFUSE.getCode()) {
                    channelUserRepository.delete(channelUserEntity);
                }

                response = ResponseVo.builder()
                        .code(SUCCESS.getCode())
                        .message(SUCCESS.getMessage())
                        .build();
            } else if (channelUserEntity.getUserState() != INVITE_WAIT.getCode()) {
                response = ResponseVo.builder()
                        .code(EXIST_INVITE_CHANNEL_USER.getCode())
                        .message(EXIST_INVITE_CHANNEL_USER.getMessage())
                        .build();
            } else {
                response = ResponseVo.builder()
                        .code(UNAUTHORIZED_INVITE_CHANNEL_USER.getCode())
                        .message(UNAUTHORIZED_INVITE_CHANNEL_USER.getMessage())
                        .build();
            }
        } else {
            response = ResponseVo.builder()
                    .code(NO_EXIST_INVITE_CHANNEL_USER.getCode())
                    .message(NO_EXIST_INVITE_CHANNEL_USER.getMessage())
                    .build();
        }

        return response;
    }

    @Override
    public ResponseVo postChannelMetadata(String userPkId, ChannelMetadataVo channelMetadataVo) {
        ResponseVo response;
        try {
            Optional<Channel> channel = channelRepository.findById(channelMetadataVo.getChannelUrl());

            if (channel.isPresent()) {
                Channel channelEntity = channel.get();

                // 방장 권한 체크
                if (channelEntity.getOwner().getId().equals(userPkId)) {
                    Optional<ChannelMetaData> channelMetaData = channelMetaDataRepository.findByChannelChannelUrl(channelMetadataVo.getChannelUrl());

                    if (channelMetaData.isPresent()) {
                        response = ResponseVo.builder()
                                .code(EXIST_CHANNEL_METADATA.getCode())
                                .message(EXIST_CHANNEL_METADATA.getMessage())
                                .build();
                    } else { // 메타 데이터 추가
                        ObjectMapper objectMapper = new ObjectMapper();
                        String metadata = objectMapper.writeValueAsString(channelMetadataVo.getMetadata());

                        channelMetaDataRepository.save(ChannelMetaData.builder()
                                .channel(channelEntity)
                                .metadata(metadata)
                                .build());

                        response = ResponseVo.builder()
                                .code(SUCCESS.getCode())
                                .message(SUCCESS.getMessage())
                                .build();
                    }
                } else {
                    response = ResponseVo.builder()
                            .code(UNAUTHORIZED_CHANNEL.getCode())
                            .message(UNAUTHORIZED_CHANNEL.getMessage())
                            .build();
                }
            } else {
                response = ResponseVo.builder()
                        .code(NO_EXIST_CHANNEL.getCode())
                        .message(NO_EXIST_CHANNEL.getMessage())
                        .build();
            }
        } catch (JsonProcessingException exception) {
            response = ResponseVo.builder()
                    .code(JSON_PARSE_ERROR.getCode())
                    .message(JSON_PARSE_ERROR.getMessage())
                    .build();
        }

        return response;
    }

    @Override
    public ResponseVo updateChannelMetadata(String userPkId, ChannelMetadataVo channelMetadataVo) {

        // TODO 등록 수정 메소드 합치기기
       return null;
    }

    private void batchInsertEntities(List<ChannelUser> entities) {

        for (int i = 0; i < entities.size(); i++) {
            entityManager.persist(entities.get(i));

            // 배치 크기 50으로 설정
            if ((i + 1) % 50 == 0 || i == entities.size() - 1) {
                entityManager.flush();
                entityManager.clear();
            }
        }
    }

    private boolean emptyAndNullCheck(String data) {
        return data != null && !data.isEmpty() && !data.isBlank();
    }
}
