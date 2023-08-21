package com.example.chatting_server.serviceImpl;

import com.example.chatting_server.entity.User;
import com.example.chatting_server.entity.UserFriend;
import com.example.chatting_server.repository.UserFriendRepository;
import com.example.chatting_server.repository.UserRepository;
import com.example.chatting_server.service.FriendService;
import com.example.chatting_server.vo.response.ResponseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.chatting_server.util.ChatCode.INVITE_ACCEPT;
import static com.example.chatting_server.util.ChatCode.INVITE_WAIT;
import static com.example.chatting_server.util.ResponseCode.*;
import static com.example.chatting_server.util.ResponseCode.NO_EXIST_REQUEST_FRIEND;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {
    final UserFriendRepository userFriendRepository;
    final UserRepository userRepository;

    @Transactional
    @Override
    public ResponseVo postFriend(String id, String nickname) {
        User user = userRepository.findByNickName(nickname);
        ResponseVo response;

        if (user == null) {
            response = ResponseVo.builder()
                    .code(NO_EXIST_USER.getCode())
                    .message(NO_EXIST_USER.getMessage())
                    .build();
        } else {
            userFriendRepository.save(UserFriend.builder()
                    .ownerUser(User.builder()
                            .id(id)
                            .build())
                    .friendUser(user)
                    .userStatus(INVITE_WAIT.getCode())
                    .inviteTime(LocalDateTime.now())
                    .build());

            response = ResponseVo.builder()
                    .code(SUCCESS.getCode())
                    .message(SUCCESS.getMessage())
                    .build();
        }

        return response;
    }

    @Transactional
    @Override
    public ResponseVo friendAccept(String id, String friendId) {
        Optional<UserFriend> userFriend = userFriendRepository.findById(friendId);
        ResponseVo response;

        if (userFriend.isPresent()) {
            UserFriend userFriendEntity = userFriend.get();

            if (userFriendEntity.getFriendUser().getId().equals(id)) {
                userFriendRepository.save(UserFriend.builder()
                        .id(friendId)
                        .friendUser(userFriendEntity.getFriendUser())
                        .ownerUser(userFriendEntity.getOwnerUser())
                        .userStatus(INVITE_ACCEPT.getCode())
                        .inviteTime(userFriendEntity.getInviteTime())
                        .build());

                response = ResponseVo.builder()
                        .code(SUCCESS.getCode())
                        .message(SUCCESS.getMessage())
                        .build();
            } else { // 수락/거절 권한이 없을 경우
                response = ResponseVo.builder()
                        .code(UNAUTHORIZED_REQUEST_FRIEND.getCode())
                        .message(UNAUTHORIZED_REQUEST_FRIEND.getMessage())
                        .build();
            }

        } else {
            response = ResponseVo.builder()
                    .code(NO_EXIST_REQUEST_FRIEND.getCode())
                    .message(NO_EXIST_REQUEST_FRIEND.getMessage())
                    .build();
        }
        return response;
    }

    @Transactional
    @Override
    public ResponseVo friendRefuse(String id, String friendId) {
        Optional<UserFriend> userFriend = userFriendRepository.findById(friendId);
        ResponseVo response;

        if (userFriend.isPresent()) {
            if (userFriend.get().getFriendUser().getId().equals(id) && userFriend.get().getUserStatus() == INVITE_WAIT.getCode()) {
                userFriendRepository.delete(userFriend.get());

                response = ResponseVo.builder()
                        .code(SUCCESS.getCode())
                        .message(SUCCESS.getMessage())
                        .build();
            } else { // 수락/거절 권한이 없을 경우
                response = ResponseVo.builder()
                        .code(UNAUTHORIZED_REQUEST_FRIEND.getCode())
                        .message(UNAUTHORIZED_REQUEST_FRIEND.getMessage())
                        .build();
            }
        } else {
            response = ResponseVo.builder()
                    .code(NO_EXIST_REQUEST_FRIEND.getCode())
                    .message(NO_EXIST_REQUEST_FRIEND.getMessage())
                    .build();
        }
        return response;
    }

}
