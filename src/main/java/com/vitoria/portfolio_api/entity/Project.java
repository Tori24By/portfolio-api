package com.vitoria.portfolio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk_project;

    private String title;
    private String type;
    private String about;
    private String image_url;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ProjectHighlight> highlights;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ProjectTechnology> technologies;
}