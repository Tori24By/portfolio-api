package com.vitoria.portfolio_api.controller;

import com.vitoria.portfolio_api.entity.Tag;
import com.vitoria.portfolio_api.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@CrossOrigin(origins = "*")
public class TagController {

    @Autowired
    private TagRepository repository;

    @GetMapping
    public List<Tag> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Tag criar(@RequestBody Tag tag) {
        return repository.save(tag);
    }

    @PutMapping("/{id}")
    public Tag atualizar(@PathVariable Integer id, @RequestBody Tag tagAtualizada) {
        return repository.findById(id).map(existente -> {
            existente.setName(tagAtualizada.getName());
            return repository.save(existente);
        }).orElseThrow(() -> new RuntimeException("Tag não encontrada: " + id));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        repository.deleteById(id);
    }
}
