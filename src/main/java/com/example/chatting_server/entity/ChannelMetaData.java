package com.example.chatting_server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelMetaData {
    @Id
    private long channelSeq;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Channel channel;

    @Column(columnDefinition = "JSON")
    private String metadata;
}
