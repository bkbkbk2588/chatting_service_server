package com.example.chatting_server.serviceImpl;

import com.example.chatting_server.entity.User;
import com.example.chatting_server.entity.UserMetadata;
import com.example.chatting_server.repository.UserMetaDataRepository;
import com.example.chatting_server.repository.UserRepository;
import com.example.chatting_server.security.component.TokenProvider;
import com.example.chatting_server.service.UserService;
import com.example.chatting_server.vo.request.*;
import com.example.chatting_server.vo.response.ResponseVo;
import com.example.chatting_server.vo.response.TokenVo;
import com.example.chatting_server.vo.response.UserInfoVo;
import com.example.chatting_server.vo.response.UserMetadataVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.example.chatting_server.util.ResponseCode.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    final String BLACK_LIST = "blackList";

    final UserRepository userRepository;

    final UserMetaDataRepository userMetaDataRepository;

    final TokenProvider tokenProvider;

    final RedisTemplate<String, String> redisTemplate;

    @Override
    public ResponseVo login(LoginVo loginVo) {
        User user = userRepository.findFirstByUserId(loginVo.getId());
        ResponseVo responseVo;

        // 로그인 성공할 경우
        if (user != null && BCrypt.checkpw(loginVo.getPw(), user.getPassword())) {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginVo.getId(), user.getId(), Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken); // security context 저장

            responseVo = ResponseVo.builder()
                    .code(SUCCESS.getCode())
                    .message(SUCCESS.getMessage())
                    .data(TokenVo.builder()
                            .accessToken(tokenProvider.createAccessToken(authenticationToken))
                            .refreshToken(tokenProvider.createRefreshToken(authenticationToken))
                            .build())
                    .build();

        } else {
            responseVo = ResponseVo.builder()
                    .code(INCORRECT_ID_OR_PASSWORD.getCode())
                    .message(INCORRECT_ID_OR_PASSWORD.getMessage())
                    .build();
        }

        return responseVo;
    }

    @Override
    public ResponseVo logout(String accessToken, String refreshToken) {
        insertTokenBlackList(accessToken, refreshToken);

        return ResponseVo.builder()
                .code(SUCCESS.getCode())
                .message(SUCCESS.getMessage())
                .build();
    }

    @Override
    public ResponseVo updateToken(String userId, String id, String refreshToken) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, id, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken); // security context 저장

        String refresh = refreshToken;

        if (StringUtils.hasText(refreshToken) && refreshToken.startsWith("Bearer ")) {
            refresh = refreshToken.substring(7);
        }

        long refreshExpireSeconds = tokenProvider.getExpireSeconds(refresh);

        // 기존 refresh 토큰 블랙리스트 등록
        redisTemplate.opsForValue().set(refresh, BLACK_LIST, refreshExpireSeconds, TimeUnit.SECONDS);

        return ResponseVo.builder()
                .code(SUCCESS.getCode())
                .message(SUCCESS.getMessage())
                .data(TokenVo.builder()
                        .accessToken(tokenProvider.createAccessToken(authenticationToken))
                        .refreshToken(tokenProvider.createRefreshToken(authenticationToken))
                        .build())
                .build();
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
                    .id(user.getId())
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

    @Override
    public ResponseVo getUser(String id) {
        ResponseVo responseVo;
        UserInfoVo userInfo = userRepository.findByUserInfo(id);

        if (userInfo == null) {
            responseVo = ResponseVo.builder()
                    .code(NO_EXIST_USER.getCode())
                    .message(NO_EXIST_USER.getMessage())
                    .build();
        } else {
            responseVo = ResponseVo.builder()
                    .code(SUCCESS.getCode())
                    .message(SUCCESS.getMessage())
                    .data(userInfo)
                    .build();
        }

        return responseVo;
    }

    @Override
    @Transactional
    public ResponseVo updateUser(UpdateUserVo updateUserVo, String id) {
        ResponseVo response;
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            response = ResponseVo.builder()
                    .code(NO_EXIST_USER.getCode())
                    .message(NO_EXIST_USER.getMessage())
                    .build();
        } else {
            String updateNickName = updateUserVo.getNickName(),
                    updatePhoneNumber = updateUserVo.getPhoneNumber();

            if (updateNickName == null || updateNickName.isBlank()) {
                updateNickName = user.getNickName();
            }

            if (updatePhoneNumber == null || updatePhoneNumber.isBlank()) {
                updatePhoneNumber = user.getPhoneNumber();
            }

            User createUser = User.builder()
                    .id(user.getId())
                    .userId(user.getUserId())
                    .password(user.getPassword())
                    .nickName(updateNickName)
                    .phoneNumber(updatePhoneNumber)
                    .userStatus(user.getUserStatus())
                    .build();

            User updateUser = userRepository.save(createUser);

            if (updateUser == null) {
                response = ResponseVo.builder()
                        .code(UPDATE_USER_FAIL.getCode())
                        .message(UPDATE_USER_FAIL.getMessage())
                        .build();
            } else {
                response = ResponseVo.builder()
                        .code(SUCCESS.getCode())
                        .message(SUCCESS.getMessage())
                        .build();
            }
        }

        return response;
    }

    @Override
    @Transactional
    public ResponseVo deleteUser(String userPkId, String accessToken, String refreshToken) {
        ResponseVo response;
        User user = userRepository.findById(userPkId).orElse(null);

        if (user == null) {
            response = ResponseVo.builder()
                    .code(NO_EXIST_USER.getCode())
                    .message(NO_EXIST_USER.getMessage())
                    .build();
        } else {
            UserMetadata userMetadata = userMetaDataRepository.findMetadataByUserId(userPkId);

            // 연관된 메타데이터 삭제
            if (userMetadata != null) {
                userMetaDataRepository.deleteById(userMetadata.getId());
            }

            userRepository.delete(user);

            // 토큰 블랙리스트에 등록
            insertTokenBlackList(accessToken, refreshToken);

            response = ResponseVo.builder()
                    .code(SUCCESS.getCode())
                    .message(SUCCESS.getMessage())
                    .build();
        }

        return response;
    }

    @Transactional
    @Override
    public ResponseVo createMetadata(String id, CreateUpdateUserMetadataVo createUserMetadataVo) {
        ResponseVo response;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String metadata = objectMapper.writeValueAsString(createUserMetadataVo.getMetadata());

            userMetaDataRepository.save(UserMetadata.builder()
                    .metadata(metadata)
                    .user(User.builder()
                            .id(id)
                            .build())
                    .build());

            response = ResponseVo.builder()
                    .code(SUCCESS.getCode())
                    .message(SUCCESS.getMessage())
                    .build();

        } catch (JsonProcessingException e) {
            response = ResponseVo.builder()
                    .code(INVALID_PARAM_VALUE.getCode())
                    .message(INVALID_PARAM_VALUE.getMessage() + " (metadata)")
                    .build();
        }

        return response;
    }

    @Override
    public ResponseVo getMetadata(String id, String userId) {
        ResponseVo response;

        UserMetadata userMetadata = userMetaDataRepository.findMetadataByUserId(id);

        if (userMetadata == null) {
            response = ResponseVo.builder()
                    .code(NO_EXIST_USER_METADATA.getCode())
                    .message(NO_EXIST_USER_METADATA.getMessage())
                    .build();
        } else {
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                Map<String, Object> metadata = objectMapper.readValue(userMetadata.getMetadata(), Map.class);

                response = ResponseVo.builder()
                        .code(SUCCESS.getCode())
                        .message(SUCCESS.getMessage())
                        .data(UserMetadataVo.builder()
                                .userId(userId)
                                .metadata(metadata).build())
                        .build();
            } catch (JsonProcessingException e) {
                e.printStackTrace();

                response = ResponseVo.builder()
                        .code(FAIL.getCode())
                        .message(FAIL.getMessage())
                        .build();
            }
        }
        return response;
    }

    @Transactional
    @Override
    public ResponseVo updateMetadata(String id, CreateUpdateUserMetadataVo createUpdateUserMetadataVo) {
        ResponseVo response;
        UserMetadata userMetadata = userMetaDataRepository.findMetadataByUserId(id);

        if (userMetadata == null) {
            response = ResponseVo.builder()
                    .code(NO_EXIST_USER_METADATA.getCode())
                    .message(NO_EXIST_USER_METADATA.getMessage())
                    .build();
        } else {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String metadata = objectMapper.writeValueAsString(createUpdateUserMetadataVo.getMetadata());

                userMetaDataRepository.save(UserMetadata.builder()
                        .id(userMetadata.getId())
                        .metadata(metadata)
                        .user(User.builder()
                                .id(id)
                                .build())
                        .build());

                response = ResponseVo.builder()
                        .code(SUCCESS.getCode())
                        .message(SUCCESS.getMessage())
                        .build();

            } catch (JsonProcessingException e) {
                response = ResponseVo.builder()
                        .code(INVALID_PARAM_VALUE.getCode())
                        .message(INVALID_PARAM_VALUE.getMessage() + " (metadata)")
                        .build();
            }
        }


        return response;
    }

    @Transactional
    @Override
    public ResponseVo deleteMetadata(String id) {
        ResponseVo response;
        UserMetadata userMetadata = userMetaDataRepository.findMetadataByUserId(id);

        if (userMetadata == null) {
            response = ResponseVo.builder()
                    .code(NO_EXIST_USER_METADATA.getCode())
                    .message(NO_EXIST_USER_METADATA.getMessage())
                    .build();
        } else {
            userMetaDataRepository.delete(userMetadata);

            response = ResponseVo.builder()
                    .code(SUCCESS.getCode())
                    .message(SUCCESS.getMessage())
                    .build();
        }
        return response;
    }

    private void insertTokenBlackList(String accessToken, String refreshToken) {
        String access = accessToken,
                refresh = refreshToken;

        if (StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer ")) {
            access = accessToken.substring(7);
        }

        if (StringUtils.hasText(refreshToken) && refreshToken.startsWith("Bearer ")) {
            refresh = refreshToken.substring(7);
        }

        long accessExpireSeconds = tokenProvider.getExpireSeconds(access),
                refreshExpireSeconds = tokenProvider.getExpireSeconds(refresh);

        // 기존 토큰 블랙리스트 등록
        redisTemplate.opsForValue().set(access, BLACK_LIST, accessExpireSeconds, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(refresh, BLACK_LIST, refreshExpireSeconds, TimeUnit.SECONDS);
    }
}
