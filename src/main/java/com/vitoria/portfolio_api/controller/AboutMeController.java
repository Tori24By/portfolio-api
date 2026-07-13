package com.vitoria.portfolio_api.controller;

import com.vitoria.portfolio_api.entity.AboutMe;
import com.vitoria.portfolio_api.repository.AboutMeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sobre-mim")
@CrossOrigin(origins = "*")
public class AboutMeController {

    @Autowired
    private AboutMeRepository repository;

    @GetMapping
    public List<AboutMe> listar() {
        return repository.findAll();
    }

    @PostMapping
    public AboutMe criar(@RequestBody AboutMe aboutMe) {
        return repository.save(aboutMe);
    }

    @PutMapping("/{id}")
    public AboutMe atualizar(@PathVariable Integer id, @RequestBody AboutMe aboutAtualizado) {
        return repository.findById(id).map(existente -> {
            existente.setDescription(aboutAtualizado.getDescription());
            existente.setNumber_projects(aboutAtualizado.getNumber_projects());
            existente.setNumber_technologies(aboutAtualizado.getNumber_technologies());
            return repository.save(existente);
        }).orElseThrow(() -> new RuntimeException("Registro AboutMe não encontrado com o ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        repository.deleteById(id);
    }
}