package com.vitoria.portfolio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk_post;

    private String platform;
    private String title;
    private String description;
    private LocalDate publish_date;
    private String url_link;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostTag> tags;
}