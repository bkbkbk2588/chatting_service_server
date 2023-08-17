package com.example.chatting_server.customRepository;

import com.example.chatting_server.entity.UserMetadata;

public interface CustomUserMetaDataRepository {
    UserMetadata findMetadataByUserId(String userId);
}
