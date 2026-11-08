package com.vitoria.portfolio_api.controller;

import com.vitoria.portfolio_api.entity.Technology;
import com.vitoria.portfolio_api.repository.TechnologyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tecnologias")
@CrossOrigin(origins = "*")
public class TechnologyController {

    @Autowired
    private TechnologyRepository repository;

    @GetMapping
    public List<Technology> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Technology criar(@RequestBody Technology technology) {
        return repository.save(technology);
    }

    @PutMapping("/{id}")
    public Technology atualizar(@PathVariable Integer id, @RequestBody Technology tecnologiaAtualizada) {
        return repository.findById(id).map(existente -> {
            existente.setName(tecnologiaAtualizada.getName());
            existente.setCategory(tecnologiaAtualizada.getCategory());
            return repository.save(existente);
        }).orElseThrow(() -> new RuntimeException("Tecnologia não encontrada: " + id));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        repository.deleteById(id);
    }
}
