package com.example.chatting_server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long channelSeq;

    @Column(unique = true)
    private String channelUrl;

    @Column
    private String channelName;

    @Column
    private LocalDateTime createTime;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "userId") // 외래 키 컬럼명 지정
    private User owner;

    @Column
    private String lastMessage;

    @Column
    private LocalDateTime lastMessageTime;
}
