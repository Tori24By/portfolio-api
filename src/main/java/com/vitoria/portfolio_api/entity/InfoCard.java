package com.vitoria.portfolio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Info_Cards")
public class InfoCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk_info_card;

    private String title;
    private String description;
    private String icon;

    @ManyToOne
    @JoinColumn(name = "fk_about_me")
    private AboutMe aboutMe;
}