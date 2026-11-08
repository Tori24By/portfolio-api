package com.vitoria.portfolio_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "projects_technologies",
        uniqueConstraints = @UniqueConstraint(columnNames = {"fk_project", "fk_technology"}))
public class ProjectTechnology {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk_project_tech;

    @ManyToOne
    @JoinColumn(name = "fk_project", nullable = false)
    @JsonIgnoreProperties({"highlights", "technologies"})
    private Project project;

    @ManyToOne
    @JoinColumn(name = "fk_technology", nullable = false)
    private Technology technology;
}
