package com.vitoria.portfolio_api.controller;

import com.vitoria.portfolio_api.entity.Post;
import com.vitoria.portfolio_api.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/publicacoes")
@CrossOrigin(origins = "*")
public class PostController {

    @Autowired
    private PostRepository repository;

    @GetMapping
    public List<Post> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Post criar(@RequestBody Post post) {
        return repository.save(post);
    }
}