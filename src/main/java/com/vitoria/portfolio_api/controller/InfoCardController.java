package com.vitoria.portfolio_api.controller;
import com.vitoria.portfolio_api.entity.InfoCard;
import com.vitoria.portfolio_api.repository.InfoCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/info-cards")
@CrossOrigin(origins = "*")
public class InfoCardController {

    @Autowired
    private InfoCardRepository repository;

    @GetMapping
    public List<InfoCard> listar() {
        return repository.findAll();
    }

    @PostMapping
    public InfoCard criar(@RequestBody InfoCard infoCard) {
        return repository.save(infoCard);
    }

    @PutMapping("/{id}")
    public InfoCard atualizar(@PathVariable Integer id, @RequestBody InfoCard infoCardAtualizado) {
        return repository.findById(id).map(existente -> {
            existente.setTitle(infoCardAtualizado.getTitle());
            existente.setDescription(infoCardAtualizado.getDescription());
            existente.setIcon(infoCardAtualizado.getIcon());
            return repository.save(existente);
        }).orElseThrow(() -> new RuntimeException("InfoCard não encontrado: " + id));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) { repository.deleteById(id); }
}