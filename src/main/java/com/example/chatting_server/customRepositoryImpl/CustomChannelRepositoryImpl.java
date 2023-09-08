package com.example.chatting_server.customRepositoryImpl;

import com.example.chatting_server.customRepository.CustomChannelRepository;

import static com.example.chatting_server.entity.QChannel.channel;
import static com.example.chatting_server.entity.QChannelMetaData.channelMetaData;
import static com.example.chatting_server.entity.QChannelUser.channelUser;

import static com.example.chatting_server.entity.QUser.user;
import static com.example.chatting_server.util.ChatCode.INVITE_ACCEPT;

import com.example.chatting_server.vo.request.ChannelListVo;
import com.example.chatting_server.vo.response.ChannelInfoResultVo;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CustomChannelRepositoryImpl implements CustomChannelRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ChannelInfoResultVo> getChannelList(String userPkId, ChannelListVo channelListVo) {
        long page = channelListVo.getPage(),
                size = channelListVo.getSize();

        return queryFactory.select(Projections.constructor(ChannelInfoResultVo.class,
                        channel.channelUrl,
                        channel.createTime,
                        user.nickName,
                        channel.channelName,
                        channel.lastMessage,
                        channel.lastMessageTime,
                        channelMetaData.metadata,
                        channelMetaData.id))
                .from(channel)

                .leftJoin(channelMetaData)
                .on(channel.channelUrl.eq(channelMetaData.channel.channelUrl))

                .innerJoin(channelUser)
                .on(channelUser.channel.channelUrl.eq(channel.channelUrl))

                .leftJoin(user)
                .on(user.id.eq(channelUser.user.id))

                .where(channelUser.user.id.eq(userPkId)
                        .and(channelUser.userState.eq(INVITE_ACCEPT.getCode())))
                .limit(channelListVo.getSize())
                .offset((page - 1L) * size)
                .fetch();
    }
}
