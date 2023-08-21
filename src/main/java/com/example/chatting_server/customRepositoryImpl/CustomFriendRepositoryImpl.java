package com.example.chatting_server.customRepositoryImpl;

import com.example.chatting_server.customRepository.CustomFriendRepository;
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
        // TODO 쿼리 조인해서 하기
//        queryFactory.select(Projections)

        return null;
    }
}
