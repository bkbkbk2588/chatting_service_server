package com.example.chatting_server.serviceImpl;

import com.example.chatting_server.entity.Channel;
import com.example.chatting_server.entity.User;
import com.example.chatting_server.repository.ChannelMetaDataRepository;
import com.example.chatting_server.repository.ChannelRepository;
import com.example.chatting_server.repository.ChannelUserRepository;
import com.example.chatting_server.repository.UserRepository;
import com.example.chatting_server.service.ChannelService;
import com.example.chatting_server.vo.request.PostChannelVo;
import com.example.chatting_server.vo.response.ResponseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.chatting_server.util.ResponseCode.NO_EXIST_USER;

@Service
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {
    final ChannelRepository channelRepository;

    final ChannelUserRepository channelUserRepository;

    final ChannelMetaDataRepository channelMetaDataRepository;

    final UserRepository userRepository;

    private final String CHANNEL_NAME = "Channel Name";

    @Transactional
    @Override
    public ResponseVo postChannel(String userPkId, PostChannelVo postChannelVo) {
        /*
            TODO
                1. 초대 받은 id가 존재 하는 유저인지 확인 / 친구 목록에 있는지도 같이 확인
                2. 3개 테이블에 알맞은 값을 insert
         */
        ResponseVo response;

        List<String> inviteNickNameList = postChannelVo.getInviteNickNameList();

        /*
            TODO
                1. 자기자신 id로 user랑 friend 목록 조인
                2. 그리고 친구목록에서 user의 nickname 맞는지 확인

                select * from user a left join user_friend b
                on a.id = b.friend_id
                where a.id = 'USER5'
         */
        Optional<List<User>> inviteUserList = userRepository.findByNickNameIn(inviteNickNameList);

        if (inviteUserList.isPresent()) {
            if (inviteUserList.get().size() != inviteNickNameList.size()) {
                response = ResponseVo.builder()
                        .code(NO_EXIST_USER.getCode())
                        .message(NO_EXIST_USER.getMessage())
                        .build();
            } else {
                channelRepository.save(Channel.builder()
                                .channelName(postChannelVo.getChannelName() == null ? CHANNEL_NAME : postChannelVo.getChannelName())
                                .createTime(LocalDateTime.now())
                        .build());
            }
        } else {
            response = ResponseVo.builder()
                    .code(NO_EXIST_USER.getCode())
                    .message(NO_EXIST_USER.getMessage())
                    .build();
        }

        return null;
    }
}
