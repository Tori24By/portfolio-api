package com.vitoria.portfolio_api.controller;

import com.vitoria.portfolio_api.entity.AboutMe;
import com.vitoria.portfolio_api.repository.AboutMeRepository;
import com.vitoria.portfolio_api.repository.ProjectRepository;
import com.vitoria.portfolio_api.repository.TechnologyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sobre-mim")
@CrossOrigin(origins = "*")
public class AboutMeController {

    @Autowired
    private AboutMeRepository repository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TechnologyRepository technologyRepository;

    @GetMapping
    public List<AboutMe> listar() {
        List<AboutMe> aboutMeList = repository.findAll();
        aboutMeList.forEach(this::preencherContadores);
        return aboutMeList;
    }

    @PostMapping
    public AboutMe criar(@RequestBody AboutMe aboutMe) {
        AboutMe salvo = repository.save(aboutMe);
        preencherContadores(salvo);
        return salvo;
    }

    @PutMapping("/{id}")
    public AboutMe atualizar(@PathVariable Integer id, @RequestBody AboutMe aboutAtualizado) {
        return repository.findById(id).map(existente -> {
            existente.setDescription(aboutAtualizado.getDescription());
            AboutMe salvo = repository.save(existente);
            preencherContadores(salvo);
            return salvo;
        }).orElseThrow(() -> new RuntimeException("Registro AboutMe não encontrado com o ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        repository.deleteById(id);
    }

    private void preencherContadores(AboutMe aboutMe) {
        aboutMe.setNumber_projects((int) projectRepository.count());
        aboutMe.setNumber_technologies((int) technologyRepository.count());
    }
}
