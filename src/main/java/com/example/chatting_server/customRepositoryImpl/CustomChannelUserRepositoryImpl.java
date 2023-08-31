package com.example.chatting_server.customRepositoryImpl;

import com.example.chatting_server.customRepository.CustomChannelUserRepository;
import com.example.chatting_server.entity.ChannelUser;

import static com.example.chatting_server.entity.QChannelUser.channelUser;

import static com.example.chatting_server.entity.QUser.user;
import static com.example.chatting_server.util.ChatCode.INVITE_ACCEPT;

import com.example.chatting_server.vo.response.ChannelActiveUserVo;
import com.example.chatting_server.vo.response.InviteChannelVo;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CustomChannelUserRepositoryImpl implements CustomChannelUserRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public ChannelUser getChannelUser(String channelUrl, String id) {
        return queryFactory.selectFrom(channelUser)
                .where(channelUser.channel.channelUrl.eq(channelUrl)
                        .and(channelUser.user.id.eq(id))
                        .and(channelUser.userState.eq(1)))
                .fetchOne();
    }

    @Override
    public List<ChannelActiveUserVo> getChannelUserStateList(String channelUrl, int userState) {
        return queryFactory.select(Projections.constructor(ChannelActiveUserVo.class,
                        channelUser.id,
                        channelUser.channel.channelUrl,
                        user.id,
                        channelUser.userState,
                        user.nickName))
                .from(channelUser).leftJoin(user)
                .on(channelUser.user.id.eq(user.id))
                .where(channelUser.channel.channelUrl.eq(channelUrl)
                        .and(channelUser.userState.eq(userState)))
                .fetch();
    }

    @Override
    public long checkIncludeChannelUser(String channelUrl, String id) {
        return queryFactory.select(channelUser.count())
                .from(channelUser)
                .where(channelUser.channel.channelUrl.eq(channelUrl)
                        .and(channelUser.user.id.eq(id))
                        .and(channelUser.userState.eq(INVITE_ACCEPT.getCode())))
                .fetchFirst();
    }

    @Override
    public long getChannelInviteUser(List<String> inviteUserIdList, String channelUrl) {
        return queryFactory.select(channelUser.count())
                .from(channelUser)
                .where(channelUser.user.id.in(inviteUserIdList)
                        .and(channelUser.channel.channelUrl.eq(channelUrl)))
                .fetchFirst();
    }

    @Override
    public List<InviteChannelVo> getInviteChannelList(String id, int userState) {
        return queryFactory.select(Projections.constructor(InviteChannelVo.class,
                channelUser.id,
                channelUser.channel.channelUrl,
                channelUser.userState
                ))
                .from(channelUser)
                .where(channelUser.user.id.eq(id)
                        .and(channelUser.userState.eq(userState)))
                .fetch();
    }
}
