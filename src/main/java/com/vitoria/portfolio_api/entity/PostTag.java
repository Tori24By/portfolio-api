package com.vitoria.portfolio_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "posts_tags",
        uniqueConstraints = @UniqueConstraint(columnNames = {"fk_post", "fk_tag"}))
public class PostTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk_post_tag;

    @ManyToOne
    @JoinColumn(name = "fk_post", nullable = false)
    @JsonIgnoreProperties("tags")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "fk_tag", nullable = false)
    private Tag tag;
}
