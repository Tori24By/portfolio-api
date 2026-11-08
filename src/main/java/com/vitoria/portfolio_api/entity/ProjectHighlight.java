package com.vitoria.portfolio_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "projects_highlights")
public class ProjectHighlight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk_highlight;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "fk_project", nullable = false)
    @JsonIgnoreProperties({"highlights", "technologies"})
    private Project project;
}
