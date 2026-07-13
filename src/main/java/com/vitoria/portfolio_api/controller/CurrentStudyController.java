package com.vitoria.portfolio_api.controller;

import com.vitoria.portfolio_api.entity.CurrentStudy;
import com.vitoria.portfolio_api.repository.CurrentStudyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/estudos")
@CrossOrigin(origins = "*")
public class CurrentStudyController {

    @Autowired
    private CurrentStudyRepository repository;

    @GetMapping
    public List<CurrentStudy> listar() {
        return repository.findAll();
    }

    @PostMapping
    public CurrentStudy criar(@RequestBody CurrentStudy study) {
        return repository.save(study);
    }
}