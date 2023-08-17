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
    public UserMetadata findMetadataByUserId(String userPkId) {
        return queryFactory.selectFrom(userMetadata)
                .where(userMetadata.user.id.eq(userPkId))
                .fetchOne();
    }
}
