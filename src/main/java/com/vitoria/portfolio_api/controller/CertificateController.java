package com.vitoria.portfolio_api.controller;

import com.vitoria.portfolio_api.entity.Certificate;
import com.vitoria.portfolio_api.repository.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certificados")
@CrossOrigin(origins = "*")
public class CertificateController {

    @Autowired
    private CertificateRepository repository;

    @GetMapping
    public List<Certificate> listarTodos() {
        return repository.findAll();
    }


    @PostMapping
    public Certificate criar(@RequestBody Certificate certificado) {
        return repository.save(certificado);
    }
}