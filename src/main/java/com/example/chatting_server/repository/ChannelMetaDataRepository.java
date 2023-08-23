package com.example.chatting_server.repository;

import com.example.chatting_server.entity.ChannelMetaData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelMetaDataRepository extends JpaRepository<ChannelMetaData, String> {
}
