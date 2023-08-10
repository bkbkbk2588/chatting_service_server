package com.example.chatting_server.repository;

import com.example.chatting_server.entity.UserMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserMetaDataRepository extends JpaRepository<UserMetadata, Long> {

}
