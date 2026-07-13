package com.vitoria.portfolio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Projects_Highlights")
public class ProjectHighlight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk_highlight;

    private String description;

    @ManyToOne
    @JoinColumn(name = "fk_project")
    private Project project;
}