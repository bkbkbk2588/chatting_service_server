package com.example.chatting_server.repository;

import com.example.chatting_server.entity.ChannelUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelUserRepository extends JpaRepository<ChannelUser, String> {

}
