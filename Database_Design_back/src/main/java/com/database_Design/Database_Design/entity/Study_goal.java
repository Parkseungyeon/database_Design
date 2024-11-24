package com.database_Design.Database_Design.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Study_goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goal_id; // 기본키

    // User와의 다대일 관계 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", nullable = false) // 외래키 설정
    private User user; // 회원 ID

    private String content; // 할 일 설명

//    private Integer priority; // 우선 순위
//    private Long std_id; // Study group ID

    private Date std_goal_start_date; // 목표 설정 날짜

    private Date std_goal_end_date; // 목표 종료 날짜

    private Boolean completed = false; // 완료 여부 (기본값: false)

    @Builder
    public Study_goal(Long goal_id, String content, Boolean completed, User user, Date std_goal_start_date, Date std_goal_end_date) {
        this.goal_id = goal_id;
        this.content = content;
        this.completed = completed;
//        this.priority = priority;
        this.user = user;
        this.std_goal_end_date = std_goal_end_date;
        this.std_goal_start_date = std_goal_start_date;
    }
}