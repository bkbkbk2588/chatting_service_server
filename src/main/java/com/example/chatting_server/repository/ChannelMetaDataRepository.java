package com.example.chatting_server.repository;

import com.example.chatting_server.customRepository.CustomChannelMetadataRepository;
import com.example.chatting_server.entity.ChannelMetaData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChannelMetaDataRepository extends JpaRepository<ChannelMetaData, String>, CustomChannelMetadataRepository {
    Optional<ChannelMetaData> findByChannelChannelUrl(String channelUrl);
}
