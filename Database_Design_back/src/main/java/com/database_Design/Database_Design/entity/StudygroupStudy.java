package com.database_Design.Database_Design.entity;


import jakarta.persistence.Entity;

import java.util.Date;

@Entity
public class StudygroupStudy {

    private Long study_group_id;

    private Date study_date;

    private Long total_study_time; // 그룹 전체 학습 시간
}
