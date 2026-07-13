package com.vitoria.portfolio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Projects_Technologies")
public class ProjectTechnology {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk_tech;

    private String tech_name;

    @ManyToOne
    @JoinColumn(name = "fk_project")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "fk_tech_category")
    private TechCategory category;
}