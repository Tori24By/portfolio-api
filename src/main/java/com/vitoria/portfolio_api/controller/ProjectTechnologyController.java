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

    @PutMapping("/{id}")
    public ProjectTechnology atualizar(@PathVariable Integer id, @RequestBody ProjectTechnology techAtualizada) {
        return repository.findById(id).map(existente -> {
            existente.setTech_name(techAtualizada.getTech_name());
            return repository.save(existente);
        }).orElseThrow(() -> new RuntimeException("Tecnologia do projeto não encontrada: " + id));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) { repository.deleteById(id); }
}