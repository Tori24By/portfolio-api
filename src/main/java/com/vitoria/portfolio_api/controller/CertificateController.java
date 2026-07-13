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

    @PutMapping("/{id}")
    public Certificate atualizar(@PathVariable Integer id, @RequestBody Certificate certificadoAtualizado) {
        return repository.findById(id).map(existente -> {
            existente.setCategory(certificadoAtualizado.getCategory());
            existente.setTitle(certificadoAtualizado.getTitle());
            existente.setIssuer(certificadoAtualizado.getIssuer());
            existente.setStart_date(certificadoAtualizado.getStart_date());
            existente.setEnd_date(certificadoAtualizado.getEnd_date());
            existente.setImage_url(certificadoAtualizado.getImage_url());
            existente.setHours(certificadoAtualizado.getHours());
            existente.setCredential_url(certificadoAtualizado.getCredential_url());
            return repository.save(existente);
        }).orElseThrow(() -> new RuntimeException("Certificado não encontrado com o ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        repository.deleteById(id);
    }
}