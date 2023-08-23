package com.example.chatting_server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelUser {

    @Id
    @GeneratedValue(generator = "channelUserIdGenerator")
    @GenericGenerator(name = "channelUserIdGenerator",
            strategy = "com.example.chatting_server.config.CustomIdGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "prefix", value = "CHANNEL_USER_")
            })
    private String id;

    @ManyToOne
    @JoinColumn(name = "channel_pk_id", referencedColumnName = "channelUrl")
    private Channel channel;

    @ManyToOne
    @JoinColumn(name = "user_pk_id", referencedColumnName = "id", unique = true)
    private User user;

    // (0 : 초대 상태 / 1 : 참여 상태)
    @Column
    private int userState;

    // (0 : 정상 / 1 : 숨기기)
    @Column
    private int hideState;
}
