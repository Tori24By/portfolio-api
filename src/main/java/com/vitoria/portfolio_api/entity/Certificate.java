package com.vitoria.portfolio_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "Certificates")
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk_certificate;

    private String category;

    private String title;

    private String issuer;

    private LocalDate start_date;

    private LocalDate end_date;

    private String image_url;

    private Integer hours;

    private String credential_url;
}