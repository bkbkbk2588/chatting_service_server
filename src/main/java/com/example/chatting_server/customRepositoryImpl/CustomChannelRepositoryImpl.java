package com.example.chatting_server.customRepositoryImpl;

import com.example.chatting_server.customRepository.CustomChannelRepository;
import com.example.chatting_server.vo.request.ChannelListVo;
import com.example.chatting_server.vo.response.ChannelInfoListVo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CustomChannelRepositoryImpl implements CustomChannelRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ChannelInfoListVo> getChannelList(ChannelListVo channelListVo) {
        return null;
    }
}
