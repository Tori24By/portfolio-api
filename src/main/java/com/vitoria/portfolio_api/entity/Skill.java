package com.vitoria.portfolio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Skills")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk_skill;

    private String name;
    private Integer percentage;

    @ManyToOne
    @JoinColumn(name = "fk_category")
    private SkillCategory category;

    public void setName(String name) {
        this.name = name;
    }
}
