package com.vitoria.portfolio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk_project;

    @Column(length = 150, nullable = false)
    private String title;

    @Column(length = 100)
    private String type;

    @Column(columnDefinition = "TEXT")
    private String about;

    @Column(length = 255)
    private String image_url;

    @Column(length = 255)
    private String short_description;

    private Boolean featured;

    @Column(length = 255)
    private String live_url;

    @Column(length = 255)
    private String repo_url;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectHighlight> highlights;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectTechnology> technologies;
}
