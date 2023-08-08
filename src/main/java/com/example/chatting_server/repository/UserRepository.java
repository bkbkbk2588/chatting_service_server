package com.example.chatting_server.repository;

import com.example.chatting_server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findFirstByUserId(String userId);
    User findFirstByNickNameAndPhoneNumber(String nickName, String phoneNumber);
    User findByUserIdAndPhoneNumber(String userId, String phoneNumber);
}
