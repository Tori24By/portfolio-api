package com.vitoria.portfolio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tech_categories")
public class TechCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk_category;

    @Column(name = "category_name", length = 50, nullable = false, unique = true)
    private String category_name;
}
