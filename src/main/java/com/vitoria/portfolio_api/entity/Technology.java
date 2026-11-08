package com.vitoria.portfolio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "technologies")
public class Technology {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk_technology;

    @Column(length = 100, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "fk_tech_category")
    private TechCategory category;
}
