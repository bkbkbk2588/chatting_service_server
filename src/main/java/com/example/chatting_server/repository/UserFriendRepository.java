package com.example.chatting_server.repository;

import com.example.chatting_server.customRepository.CustomFriendRepository;
import com.example.chatting_server.entity.UserFriend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserFriendRepository extends JpaRepository<UserFriend, String>, CustomFriendRepository {

    Optional<UserFriend> findByOwnerUserIdAndFriendUserId(String ownerId, String friendId);
    Optional<List<UserFriend>> findByOwnerUserIdAndUserStatus(String id, int userStatus);
}
