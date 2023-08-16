package com.example.chatting_server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User implements Serializable {

    // TODO 1. userSeq파라미터 명 변경 2. security에 userSeq 넣기 3. 엔티티 전체적으로 수정
    @Id
    @GeneratedValue(generator = "customIdGenerator")
    @GenericGenerator(name = "customIdGenerator",
            strategy = "com.example.chatting_server.config.CustomIdGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "prefix", value = "USER_")
            })
    private String userSeq;

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
