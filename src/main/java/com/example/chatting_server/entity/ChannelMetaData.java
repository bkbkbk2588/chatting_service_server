package com.example.chatting_server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChannelMetaData {
    @Id
    @GeneratedValue(generator = "channelMetadataIdGenerator")
    @GenericGenerator(name = "channelMetadataIdGenerator",
            strategy = "com.example.chatting_server.config.CustomIdGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "prefix", value = "CHANNEL_METADATA_")
            })
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_pk_id", referencedColumnName = "channelUrl", unique = true)
    private Channel channel;

    @Column(columnDefinition = "JSON")
    private String metadata;
}
