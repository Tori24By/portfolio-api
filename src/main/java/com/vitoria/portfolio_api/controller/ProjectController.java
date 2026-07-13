package com.vitoria.portfolio_api.controller;

import com.vitoria.portfolio_api.entity.Project;
import com.vitoria.portfolio_api.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/projetos")
@CrossOrigin(origins = "*")
public class ProjectController {

    @Autowired
    private ProjectRepository repository;

    @GetMapping
    public List<Project> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Project criar(@RequestBody Project project) {
        return repository.save(project);
    }

    @PutMapping("/{id}")
    public Project atualizar(@PathVariable Integer id, @RequestBody Project projetoAtualizado) {
        return repository.findById(id).map(projetoExistente -> {
            projetoExistente.setTitle(projetoAtualizado.getTitle());
            projetoExistente.setType(projetoAtualizado.getType());
            projetoExistente.setAbout(projetoAtualizado.getAbout());
            projetoExistente.setImage_url(projetoAtualizado.getImage_url());
            projetoExistente.setShort_description(projetoAtualizado.getShort_description());
            projetoExistente.setFeatured(projetoAtualizado.getFeatured());
            projetoExistente.setLive_url(projetoAtualizado.getLive_url());
            projetoExistente.setRepo_url(projetoAtualizado.getRepo_url());

            return repository.save(projetoExistente);
        }).orElseThrow(() -> new RuntimeException("Projeto não encontrado com o ID: " + id));
    }

    // Rota DELETE: Apaga um projeto
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        repository.deleteById(id);
    }
}