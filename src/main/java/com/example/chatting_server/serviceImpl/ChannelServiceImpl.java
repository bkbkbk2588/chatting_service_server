package com.example.chatting_server.serviceImpl;

import com.example.chatting_server.entity.*;
import com.example.chatting_server.repository.ChannelMetaDataRepository;
import com.example.chatting_server.repository.ChannelRepository;
import com.example.chatting_server.repository.ChannelUserRepository;
import com.example.chatting_server.repository.UserRepository;
import com.example.chatting_server.service.ChannelService;
import com.example.chatting_server.vo.request.PostChannelVo;
import com.example.chatting_server.vo.response.FriendUserInfoVo;
import com.example.chatting_server.vo.response.ResponseVo;
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

    @Transactional
    @Override
    public ResponseVo postChannel(String userPkId, PostChannelVo postChannelVo) {
        ResponseVo response = null;

        List<String> inviteNickNameList = postChannelVo.getInviteNickNameList();

        /*
            TODO
                1. 자기자신 id로 user랑 friend 목록 조인
                2. 그리고 친구목록에서 user의 nickname 맞는지 확인
         */
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
                            .channelName(postChannelVo.getChannelName() == null ? CHANNEL_NAME : postChannelVo.getChannelName())
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
                            .userState(INVITE_WAIT.getCode())
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

//                    channelUserRepository.saveAll(channelUserList);

                    batchInsertEntities(channelUserList);

                    if (postChannelVo.getMetaData() != null) {
                        channelMetaDataRepository.save(ChannelMetaData.builder()
                                .channel(channel)
                                .metadata(postChannelVo.getMetaData())
                                .build());
                    }


                    Map<String, String> dataMap = new HashMap<>();

//                    dataMap.put("channelUrl", channel);
                    response = ResponseVo.builder()
                            .code(SUCCESS.getCode())
                            .message(SUCCESS.getMessage())
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
}
