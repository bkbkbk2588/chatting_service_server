package com.example.chatting_server.serviceImpl;

import com.example.chatting_server.entity.User;
import com.example.chatting_server.repository.UserRepository;
import com.example.chatting_server.service.UserService;
import com.example.chatting_server.vo.request.CreateUserVo;
import com.example.chatting_server.vo.response.ResponseVo;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static com.example.chatting_server.util.ResponseCode.ALREADY_EXIST_ID;
import static com.example.chatting_server.util.ResponseCode.SUCCESS;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    final UserRepository userRepository;

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
}
