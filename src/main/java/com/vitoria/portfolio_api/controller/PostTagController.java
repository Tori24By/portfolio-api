package com.vitoria.portfolio_api.controller;

import com.vitoria.portfolio_api.entity.PostTag;
import com.vitoria.portfolio_api.repository.PostTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post-tags")
@CrossOrigin(origins = "*")
public class PostTagController {

    @Autowired
    private PostTagRepository repository;

    @GetMapping
    public List<PostTag> listar() {
        return repository.findAll();
    }

    @PostMapping
    public PostTag criar(@RequestBody PostTag tag) {
        return repository.save(tag);
    }

    @PutMapping("/{id}")
    public PostTag atualizar(@PathVariable Integer id, @RequestBody PostTag tagAtualizada) {
        return repository.findById(id).map(existente -> {
            existente.setTag(tagAtualizada.getTag());
            return repository.save(existente);
        }).orElseThrow(() -> new RuntimeException("Tag de post não encontrada: " + id));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        repository.deleteById(id);
    }
}
