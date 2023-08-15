package com.example.chatting_server.customRepositoryImpl;

import com.example.chatting_server.customRepository.CustomUserRepository;
import com.example.chatting_server.vo.response.UserInfoVo;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.example.chatting_server.entity.QUser.user;
import static com.example.chatting_server.entity.QUserMetadata.userMetadata;

@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public UserInfoVo findByUserInfo(String userId) {
        Tuple result = queryFactory
                .select(user.userId,
                        user.nickName,
                        user.userStatus,
                        user.phoneNumber,
                        userMetadata.metadata)
                .from(user)
                .leftJoin(userMetadata)
                .on(user.userId.eq(userMetadata.user.userId))
                .where(user.userId.eq(userId))
                .fetchOne();

        if (result == null) {
            return null;
        }

        return UserInfoVo.builder()
                .userId(result.get(user.userId))
                .nickName(result.get(user.nickName))
                .userStatus(result.get(user.userStatus))
                .phoneNumber(result.get(user.phoneNumber))
                .metaData(result.get(userMetadata.metadata))
                .build();
    }
}
