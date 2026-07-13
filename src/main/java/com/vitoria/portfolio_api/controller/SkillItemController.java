package com.vitoria.portfolio_api.controller;
import com.vitoria.portfolio_api.entity.Skill;
import com.vitoria.portfolio_api.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/skills-detalhes")
@CrossOrigin(origins = "*")
public class SkillItemController {

    @Autowired
    private SkillRepository repository;

    @GetMapping
    public List<Skill> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Skill criar(@RequestBody Skill skill) {
        return repository.save(skill);
    }
}