package com.example.chatting_server.repository;

import com.example.chatting_server.customRepository.CustomUserMetaDataRepository;
import com.example.chatting_server.entity.UserMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMetaDataRepository extends JpaRepository<UserMetadata, String>, CustomUserMetaDataRepository {
}
