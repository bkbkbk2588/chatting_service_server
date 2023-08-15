package com.example.chatting_server.customRepository;

import com.example.chatting_server.entity.UserMetadata;

public interface CustomUserMetaDataRepository {
    Long findMetadataByUserId(String userId);
}
