package com.example.chatting_server.repository;

import com.example.chatting_server.entity.UserFriend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFriendRepository extends JpaRepository<UserFriend, String> {

}
