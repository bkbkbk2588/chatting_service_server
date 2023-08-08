package com.example.chatting_server.serviceImpl;

import com.example.chatting_server.entity.User;
import com.example.chatting_server.repository.UserRepository;
import com.example.chatting_server.security.component.TokenProvider;
import com.example.chatting_server.service.UserService;
import com.example.chatting_server.vo.request.CreateUserVo;
import com.example.chatting_server.vo.request.FindUserIdVo;
import com.example.chatting_server.vo.request.LoginVo;
import com.example.chatting_server.vo.request.UpdatePasswordVo;
import com.example.chatting_server.vo.response.ResponseVo;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static com.example.chatting_server.util.ResponseCode.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    final UserRepository userRepository;

    final TokenProvider tokenProvider;

    final RedisTemplate<String, Object> redisTemplate;

    @Override
    public ResponseVo login(LoginVo loginVo) {
        Map<String, String> test = new HashMap<>();

        test.put("1", "aaa");

        redisTemplate.opsForValue().set("test", test);
        
        return null;
    }

    @Override
    public ResponseVo getUserIdValidate(String userId) {
        Map<String, Boolean> data = new HashMap<>();
        String key = "duplicateId";

        User user = userRepository.findFirstByUserId(userId);

        if (user != null) {
            data.put(key, true);
        } else {
            data.put(key, false);
        }

        return ResponseVo.builder()
                .code(SUCCESS.getCode())
                .message(SUCCESS.getMessage())
                .data(data)
                .build();
    }

    @Transactional
    @Override
    public ResponseVo createUser(CreateUserVo createUserVo) {
        ResponseVo response;
        User user = userRepository.findFirstByUserId(createUserVo.getId());

        // 유저 존재 체크
        if (user != null) {
            response = ResponseVo.builder()
                    .code(ALREADY_EXIST_ID.getCode())
                    .message(ALREADY_EXIST_ID.getMessage())
                    .build();
        } else {
            String hashedPassword = BCrypt.hashpw(createUserVo.getPw(), BCrypt.gensalt());

            User createUser = User.builder()
                    .userId(createUserVo.getId())
                    .password(hashedPassword)
                    .nickName(createUserVo.getNickName())
                    .phoneNumber(createUserVo.getPhoneNumber())
                    .userStatus(0)
                    .build();

            userRepository.save(createUser);

            response = ResponseVo.builder()
                    .code(SUCCESS.getCode())
                    .message(SUCCESS.getMessage())
                    .build();

        }

        return response;
    }

    @Override
    public ResponseVo getUserId(FindUserIdVo findUserIdVo) {
        ResponseVo response;
        User user = userRepository.findFirstByNickNameAndPhoneNumber(findUserIdVo.getNickName(), findUserIdVo.getPhoneNumber());

        if (user == null) {
            response = ResponseVo.builder()
                    .code(NO_EXIST_USER_ID.getCode())
                    .message(NO_EXIST_USER_ID.getMessage())
                    .build();
        } else {
            Map<String, String> data = new HashMap<>();

            data.put("id", user.getUserId());

            response = ResponseVo.builder()
                    .code(SUCCESS.getCode())
                    .message(SUCCESS.getMessage())
                    .data(data)
                    .build();
        }
        return response;
    }

    @Transactional
    @Override
    public ResponseVo updatePassword(UpdatePasswordVo updatePasswordVo) {
        ResponseVo response;
        User user = userRepository.findByUserIdAndPhoneNumber(updatePasswordVo.getId(), updatePasswordVo.getPhoneNumber());

        if (user == null) {
            response = ResponseVo.builder()
                    .code(NO_EXIST_USER.getCode())
                    .message(NO_EXIST_USER.getMessage())
                    .build();
        } else {
            String newPassword = BCrypt.hashpw(updatePasswordVo.getNewPw(), BCrypt.gensalt());

            userRepository.save(User.builder()
                            .userSeq(user.getUserSeq())
                            .userId(user.getUserId())
                            .password(newPassword)
                            .phoneNumber(user.getPhoneNumber())
                            .nickName(user.getNickName())
                            .userStatus(user.getUserStatus())
                    .build());

            response = ResponseVo.builder()
                    .code(SUCCESS.getCode())
                    .message(SUCCESS.getMessage())
                    .build();
        }
        return response;
    }
}
