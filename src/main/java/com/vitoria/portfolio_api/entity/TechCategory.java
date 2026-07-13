package com.vitoria.portfolio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Tech_Categories")
public class TechCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk_category;

    @Column(name = "category_name")
    private String categoryName;
}