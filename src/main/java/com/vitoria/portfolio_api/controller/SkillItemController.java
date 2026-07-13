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

    @PutMapping("/{id}")
    public Skill atualizar(@PathVariable Integer id, @RequestBody Skill skillAtualizada) {
        return repository.findById(id).map(existente -> {
            existente.setName(skillAtualizada.getName());
            existente.setPercentage(skillAtualizada.getPercentage());
            return repository.save(existente);
        }).orElseThrow(() -> new RuntimeException("Skill não encontrada: " + id));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) { repository.deleteById(id); }
}