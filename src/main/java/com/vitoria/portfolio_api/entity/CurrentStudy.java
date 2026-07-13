package com.vitoria.portfolio_api.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Current_Studies")
public class CurrentStudy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk_study;

    private String category;
    private String title;
    private String description;
    private Integer percentage_progress;
}