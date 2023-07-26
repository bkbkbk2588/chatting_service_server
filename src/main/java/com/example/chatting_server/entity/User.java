package com.example.chatting_server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userSeq;

    @Column(unique = true)
    private String userId;

    @Column
    private String password;

    @Column
    private String nickName;

    // 0 : 정상, 1 : 정지, 2 : 휴먼상태
    @Column
    private int userStatus;
}
