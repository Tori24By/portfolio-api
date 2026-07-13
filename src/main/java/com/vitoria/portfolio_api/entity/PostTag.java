package com.vitoria.portfolio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Posts_Tags")
public class PostTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk_tag;

    private String tag_name;

    @ManyToOne
    @JoinColumn(name = "fk_post")
    private Post post;
}