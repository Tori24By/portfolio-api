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
}