package com.vitoria.portfolio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk_tag;

    @Column(length = 50, nullable = false, unique = true)
    private String name;
}
