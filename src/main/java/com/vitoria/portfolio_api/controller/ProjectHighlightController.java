package com.vitoria.portfolio_api.controller;
import com.vitoria.portfolio_api.entity.ProjectHighlight;
import com.vitoria.portfolio_api.repository.ProjectHighlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/destaques")
@CrossOrigin(origins = "*")
public class ProjectHighlightController {

    @Autowired
    private ProjectHighlightRepository repository;

    @GetMapping
    public List<ProjectHighlight> listar() {
        return repository.findAll();
    }

    @PostMapping
    public ProjectHighlight criar(@RequestBody ProjectHighlight highlight) {
        return repository.save(highlight);
    }
}