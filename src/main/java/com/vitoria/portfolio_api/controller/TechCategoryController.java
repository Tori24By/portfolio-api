package com.vitoria.portfolio_api.controller;
import com.vitoria.portfolio_api.entity.TechCategory;
import com.vitoria.portfolio_api.repository.TechCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categorias-tech")
@CrossOrigin(origins = "*")
public class TechCategoryController {

    @Autowired
    private TechCategoryRepository repository;

    @GetMapping
    public List<TechCategory> listar() {
        return repository.findAll();
    }

    @PostMapping
    public TechCategory criar(@RequestBody TechCategory category) {
        return repository.save(category);
    }

    @PutMapping("/{id}")
    public TechCategory atualizar(@PathVariable Integer id, @RequestBody TechCategory categoriaAtualizada) {
        return repository.findById(id).map(existente -> {
            existente.setCategoryName(categoriaAtualizada.getCategoryName());
            return repository.save(existente);
        }).orElseThrow(() -> new RuntimeException("Categoria Tech não encontrada: " + id));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) { repository.deleteById(id); }
}