package com.vitoria.portfolio_api.controller;

import com.vitoria.portfolio_api.entity.SkillCategory;
import com.vitoria.portfolio_api.repository.SkillCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/habilidades")
@CrossOrigin(origins = "*")
public class SkillController {

    @Autowired
    private SkillCategoryRepository repository;

    @GetMapping
    public List<SkillCategory> listarCategorias() {
        return repository.findAll();
    }

    @PostMapping
    public SkillCategory criarCategoria(@RequestBody SkillCategory category) {
        return repository.save(category);
    }
}