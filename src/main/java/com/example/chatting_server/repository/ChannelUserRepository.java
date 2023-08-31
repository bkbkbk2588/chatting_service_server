package com.example.chatting_server.repository;

import com.example.chatting_server.customRepository.CustomChannelUserRepository;
import com.example.chatting_server.entity.ChannelUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChannelUserRepository extends JpaRepository<ChannelUser, String>, CustomChannelUserRepository {
    Optional<List<ChannelUser>> findByChannelChannelUrl(String channelUrl);
    Optional<ChannelUser> findByChannelChannelUrlAndUserId(String channelUrl, String id);
    Optional<List<ChannelUser>> findByUserIdAndUserState(String id, int userState);
}
