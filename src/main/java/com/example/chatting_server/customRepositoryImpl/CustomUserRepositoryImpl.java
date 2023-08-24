package com.example.chatting_server.customRepositoryImpl;

import com.example.chatting_server.customRepository.CustomUserRepository;
import com.example.chatting_server.vo.response.FriendUserInfoVo;
import com.example.chatting_server.vo.response.UserInfoVo;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.chatting_server.entity.QUser.user;
import static com.example.chatting_server.entity.QUserFriend.userFriend;
import static com.example.chatting_server.entity.QUserMetadata.userMetadata;
import static com.example.chatting_server.util.ChatCode.INVITE_ACCEPT;
import static com.example.chatting_server.util.ChatCode.USER_OK;

@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public UserInfoVo findByUserInfo(String id) {
        Tuple result = queryFactory
                .select(user.userId,
                        user.nickName,
                        user.userStatus,
                        user.phoneNumber,
                        userMetadata.metadata)
                .from(user)
                .leftJoin(userMetadata)
                .on(user.id.eq(userMetadata.user.id))
                .where(user.id.eq(id))
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

    @Override
    public List<FriendUserInfoVo> findByFriendUser(String userId, List<String> inviteUserIdList) {
        return queryFactory.select(Projections.constructor(FriendUserInfoVo.class,
                        userFriend.ownerUser.id, userFriend.friendUser.id))
                .from(userFriend)
                .where(userFriend.ownerUser.id.eq(userId)
                        .and(userFriend.friendUser.id.in(inviteUserIdList))
                        .and(userFriend.userStatus.eq(INVITE_ACCEPT.getCode())))
                .fetch();
    }
}
