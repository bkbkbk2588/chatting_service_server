package com.example.chatting_server.customRepositoryImpl;

import com.example.chatting_server.customRepository.CustomChannelRepository;

import static com.example.chatting_server.entity.QChannel.channel;
import static com.example.chatting_server.entity.QChannelMetaData.channelMetaData;
import static com.example.chatting_server.entity.QChannelUser.channelUser;

import static com.example.chatting_server.entity.QUser.user;
import static com.example.chatting_server.util.ChatCode.INVITE_ACCEPT;

import com.example.chatting_server.vo.request.ChannelListVo;
import com.example.chatting_server.vo.response.ChannelDetailVo;
import com.example.chatting_server.vo.response.ChannelInfoResultVo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class CustomChannelRepositoryImpl implements CustomChannelRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ChannelInfoResultVo> getChannelList(String userPkId, ChannelListVo channelListVo) {
        long page = channelListVo.getPage(),
                size = channelListVo.getSize();

        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.hasText(channelListVo.getCreateBefore())) {
            builder.and(channel.createTime.before(LocalDateTime.parse(channelListVo.getCreateBefore())));
        }

        if (StringUtils.hasText(channelListVo.getCreatedAfter())) {
            builder.and(channel.createTime.after(LocalDateTime.parse(channelListVo.getCreatedAfter())));
        }

        if (StringUtils.hasText(channelListVo.getChannelName())) {
            builder.and(channel.channelName.eq(channelListVo.getChannelName()));
        }

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
                        .and(channelUser.userState.eq(INVITE_ACCEPT.getCode())), builder)
                .limit(channelListVo.getSize())
                .offset((page - 1L) * size)
                .fetch();
    }

    @Override
    public List<ChannelDetailVo> getChannelDetail(String userPkId, String channelUrl) {
        return queryFactory.select(Projections.constructor(ChannelDetailVo.class,
                        channel.channelUrl,
                        channel.createTime,
                        user.nickName,
                        channel.channelName,
                        channel.lastMessage,
                        channel.lastMessageTime,
                        channelMetaData.metadata,
                        channelMetaData.id,
                        user.nickName))
                .from(channel)

                .leftJoin(channelMetaData)
                .on(channel.channelUrl.eq(channelMetaData.channel.channelUrl))

                .innerJoin(channelUser)
                .on(channelUser.channel.channelUrl.eq(channel.channelUrl))

                .leftJoin(user)
                .on(user.id.eq(channelUser.user.id))

                .where(channelUser.userState.eq(INVITE_ACCEPT.getCode())
                                .and(channel.channelUrl.eq(channelUrl))
                        .and(JPAExpressions
                                .select(channelUser.user.id)
                                .from(channelUser)
                                .where(channelUser.user.id.eq(userPkId),
                                        channelUser.channel.channelUrl.eq(channelUrl)).exists())
                )
                .fetch();
    }
}
