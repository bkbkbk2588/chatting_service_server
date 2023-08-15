package com.example.chatting_server.customRepositoryImpl;

import com.example.chatting_server.customRepository.CustomUserMetaDataRepository;
import static com.example.chatting_server.entity.QUserMetadata.userMetadata;
import com.example.chatting_server.entity.UserMetadata;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomUserMetaDataRepositoryImpl implements CustomUserMetaDataRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Long findMetadataByUserId(String userId) {
        Long userSeq = queryFactory.select(userMetadata.userSeq)
                .from(userMetadata)
                .where(userMetadata.user.userId.eq(userId))
                .fetchOne();

        return userSeq;
    }
}
