package com.example.chatting_server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userSeq;

    @Column(unique = true)
    private String userId;

    @Column
    private String password;

    @Column
    private String phoneNumber;

    @Column
    private String nickName;

    // 0 : 정상, 1 : 정지, 2 : 휴먼상태
    @Column
    private int userStatus;
}
