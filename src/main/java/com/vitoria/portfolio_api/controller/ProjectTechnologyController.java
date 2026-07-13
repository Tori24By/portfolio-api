package com.vitoria.portfolio_api.controller;
import com.vitoria.portfolio_api.entity.ProjectTechnology;
import com.vitoria.portfolio_api.repository.ProjectTechnologyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tecnologias-projeto")
@CrossOrigin(origins = "*")
public class ProjectTechnologyController {

    @Autowired
    private ProjectTechnologyRepository repository;

    @GetMapping
    public List<ProjectTechnology> listar() {
        return repository.findAll();
    }

    @PostMapping
    public ProjectTechnology criar(@RequestBody ProjectTechnology tech) {
        return repository.save(tech);
    }
}