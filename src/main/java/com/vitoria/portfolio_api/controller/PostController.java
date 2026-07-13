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

    @PutMapping("/{id}")
    public Post atualizar(@PathVariable Integer id, @RequestBody Post postAtualizado) {
        return repository.findById(id).map(existente -> {
            existente.setPlatform(postAtualizado.getPlatform());
            existente.setTitle(postAtualizado.getTitle());
            existente.setDescription(postAtualizado.getDescription());
            existente.setPublish_date(postAtualizado.getPublish_date());
            existente.setUrl_link(postAtualizado.getUrl_link());
            return repository.save(existente);
        }).orElseThrow(() -> new RuntimeException("Post não encontrado com o ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        repository.deleteById(id);
    }
}