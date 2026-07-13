package com.vitoria.portfolio_api.controller;

import com.vitoria.portfolio_api.entity.AboutMe;
import com.vitoria.portfolio_api.repository.AboutMeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sobre-mim")
@CrossOrigin(origins = "*")
public class AboutMeController {

    @Autowired
    private AboutMeRepository repository;

    @GetMapping
    public List<AboutMe> listar() {
        return repository.findAll();
    }

    @PostMapping
    public AboutMe criar(@RequestBody AboutMe aboutMe) {
        return repository.save(aboutMe);
    }
}