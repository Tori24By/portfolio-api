package com.vitoria.portfolio_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "info_cards")
public class InfoCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk_info_card;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 50)
    private String icon;

    @ManyToOne
    @JoinColumn(name = "fk_about_me", nullable = false)
    @JsonIgnoreProperties("infoCards")
    private AboutMe aboutMe;
}
