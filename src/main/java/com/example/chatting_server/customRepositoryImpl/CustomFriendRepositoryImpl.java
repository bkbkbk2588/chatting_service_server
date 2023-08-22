package com.example.chatting_server.customRepositoryImpl;

import com.example.chatting_server.customRepository.CustomFriendRepository;
import static com.example.chatting_server.entity.QUserFriend.userFriend;

import static com.example.chatting_server.entity.QUser.user;
import com.example.chatting_server.vo.response.FriendUserVo;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CustomFriendRepositoryImpl implements CustomFriendRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<FriendUserVo> getFriendList(String id, int userStatus) {
        return queryFactory.select(Projections.constructor(FriendUserVo.class,
                        user.nickName,
                        userFriend.id,
                        userFriend.userStatus,
                        userFriend.inviteTime))
                .from(userFriend).join(user).on(user.id.eq(userFriend.friendUser.id))
                .where(userFriend.ownerUser.id.eq(id)
                        .and(userFriend.userStatus.eq(userStatus)))
                .fetch();
    }
}
