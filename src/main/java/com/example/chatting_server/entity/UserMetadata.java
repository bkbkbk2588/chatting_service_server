package com.example.chatting_server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMetadata {
    @Id
    private long userSeq;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private User user;

    @Column(columnDefinition = "JSON")
    private String metadata;
}
