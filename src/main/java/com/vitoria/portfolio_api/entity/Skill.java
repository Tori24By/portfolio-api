package com.vitoria.portfolio_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "skills")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk_skill;

    @Column(length = 100, nullable = false)
    private String name;

    private Integer percentage;

    @ManyToOne
    @JoinColumn(name = "fk_category", nullable = false)
    @JsonIgnoreProperties("skills")
    private SkillCategory category;
}
