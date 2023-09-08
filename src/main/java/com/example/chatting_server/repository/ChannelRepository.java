package com.example.chatting_server.repository;

import com.example.chatting_server.customRepository.CustomChannelRepository;
import com.example.chatting_server.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, String>, CustomChannelRepository {
}
