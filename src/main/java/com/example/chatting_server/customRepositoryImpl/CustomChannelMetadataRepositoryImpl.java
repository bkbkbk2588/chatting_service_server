package com.example.chatting_server.customRepositoryImpl;

import com.example.chatting_server.customRepository.CustomChannelMetadataRepository;

import static com.example.chatting_server.entity.QChannel.channel;

import static com.example.chatting_server.entity.QChannelMetaData.channelMetaData;

import com.example.chatting_server.vo.response.ChannelAndMetadataInfoVo;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CustomChannelMetadataRepositoryImpl implements CustomChannelMetadataRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public ChannelAndMetadataInfoVo getChannelMetadata(String channelUrl) {
        return queryFactory.select(Projections.constructor(ChannelAndMetadataInfoVo.class,
                        channelMetaData.id,
                        channel.channelUrl,
                        channel.owner.id
                )).from(channel).leftJoin(channelMetaData)
                .on(channel.channelUrl.eq(channelMetaData.channel.channelUrl))
                .where(channel.channelUrl.eq(channelUrl))
                .fetchFirst();
    }
}
