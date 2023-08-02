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

    /* TODO
        방 생성 시간
        방장 id
        채팅 방 마지막 메세지
        마지막 메세지 시간
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long channelSeq;

    @Column
    private LocalDateTime createTime;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "userId") // 외래 키 컬럼명 지정
    private User owner;
}
