package com.vitoria.portfolio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "current_studies")
public class CurrentStudy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk_study;

    @Column(length = 50)
    private String category;

    @Column(length = 150, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer percentage_progress;
}
