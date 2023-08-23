package com.example.chatting_server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Channel {
    @Id
    @GeneratedValue(generator = "channelIdGenerator")
    @GenericGenerator(name = "channelIdGenerator",
            strategy = "com.example.chatting_server.config.CustomIdGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "prefix", value = "CHANNEL_")
            })
    private String channelUrl;

    @Column
    private String channelName;

    @Column
    private LocalDateTime createTime;

    @ManyToOne
    @JoinColumn(name = "owner_pk_id", referencedColumnName = "id") // 외래 키 컬럼명 지정
    private User owner;

    @Column
    private String lastMessage;

    @Column
    private LocalDateTime lastMessageTime;
}
