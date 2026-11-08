package com.vitoria.portfolio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "certificates")
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk_certificate;

    @Column(length = 50)
    private String category;

    @Column(length = 150, nullable = false)
    private String title;

    @Column(length = 100)
    private String issuer;

    private LocalDate start_date;

    private LocalDate end_date;

    @Column(length = 255)
    private String image_url;

    private Integer hours;

    @Column(length = 255)
    private String credential_url;
}
