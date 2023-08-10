package com.example.chatting_server.customRepositoryImpl;

import com.example.chatting_server.customRepository.CustomUserMetaDataRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomUserMetaDataRepositoryImpl implements CustomUserMetaDataRepository {

    private final JPAQueryFactory queryFactory;

}
