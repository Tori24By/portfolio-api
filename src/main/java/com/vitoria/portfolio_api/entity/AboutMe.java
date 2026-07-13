package com.vitoria.portfolio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "About_Me")
public class AboutMe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk_about_me;

    private String description;

    private Integer number_projects;

    private Integer number_technologies;

    @OneToMany(mappedBy = "aboutMe", cascade = CascadeType.ALL)
    private List<InfoCard> infoCards;
}