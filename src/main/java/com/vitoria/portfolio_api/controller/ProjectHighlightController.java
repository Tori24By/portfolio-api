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

    @PutMapping("/{id}")
    public ProjectHighlight atualizar(@PathVariable Integer id, @RequestBody ProjectHighlight highlightAtualizado) {
        return repository.findById(id).map(existente -> {
            existente.setDescription(highlightAtualizado.getDescription());
            return repository.save(existente);
        }).orElseThrow(() -> new RuntimeException("Destaque não encontrado: " + id));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) { repository.deleteById(id); }
}