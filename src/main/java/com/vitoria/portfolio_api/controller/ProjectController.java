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
}