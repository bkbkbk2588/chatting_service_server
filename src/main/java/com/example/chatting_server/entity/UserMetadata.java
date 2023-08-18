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
public class UserMetadata {
    @Id
    @GeneratedValue(generator = "userMetadataIdGenerator")
    @GenericGenerator(name = "userMetadataIdGenerator",
            strategy = "com.example.chatting_server.config.CustomIdGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "prefix", value = "USER_METADATA_")
            })
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_pk_id", referencedColumnName = "id", unique = true)
    private User user;

    @Column(columnDefinition = "JSON")
    private String metadata;
}
