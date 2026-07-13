package com.vitoria.portfolio_api.controller;

import com.vitoria.portfolio_api.entity.CurrentStudy;
import com.vitoria.portfolio_api.repository.CurrentStudyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/estudos")
@CrossOrigin(origins = "*")
public class CurrentStudyController {

    @Autowired
    private CurrentStudyRepository repository;

    @GetMapping
    public List<CurrentStudy> listar() {
        return repository.findAll();
    }

    @PostMapping
    public CurrentStudy criar(@RequestBody CurrentStudy study) {
        return repository.save(study);
    }

    @PutMapping("/{id}")
    public CurrentStudy atualizar(@PathVariable Integer id, @RequestBody CurrentStudy estudoAtualizado) {
        return repository.findById(id).map(existente -> {
            existente.setCategory(estudoAtualizado.getCategory());
            existente.setTitle(estudoAtualizado.getTitle());
            existente.setDescription(estudoAtualizado.getDescription());
            existente.setPercentage_progress(estudoAtualizado.getPercentage_progress());
            return repository.save(existente);
        }).orElseThrow(() -> new RuntimeException("Estudo não encontrado com o ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        repository.deleteById(id);
    }
}