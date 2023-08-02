package com.example.chatting_server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long channelUserSeq;

    @ManyToOne
    @JoinColumn(name = "channel_url", referencedColumnName = "channelUrl")
    private Channel channel;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    // (0 : 초대 상태 / 1 : 참여 상태)
    @Column
    private int userState;

    // (0 : 정상 / 1 : 숨기기)
    @Column
    private int hideState;
}
