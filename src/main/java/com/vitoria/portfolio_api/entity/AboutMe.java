package com.vitoria.portfolio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "about_me")
public class AboutMe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk_about_me;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Transient
    private Integer number_projects;

    @Transient
    private Integer number_technologies;

    @OneToMany(mappedBy = "aboutMe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InfoCard> infoCards;
}
