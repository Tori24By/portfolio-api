package com.vitoria.portfolio_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitoria.portfolio_api.entity.AboutMe;
import com.vitoria.portfolio_api.entity.InfoCard;
import com.vitoria.portfolio_api.repository.InfoCardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InfoCardController.class)
class InfoCardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InfoCardRepository repository;

    private InfoCard infoCard;

    @BeforeEach
    void setUp() {
        AboutMe aboutMe = new AboutMe();
        aboutMe.setPk_about_me(1);
        aboutMe.setDescription("Sobre mim");

        infoCard = new InfoCard();
        infoCard.setPk_info_card(1);
        infoCard.setTitle("Card 1");
        infoCard.setDescription("Descrição do card");
        infoCard.setIcon("icon-url");
        infoCard.setAboutMe(aboutMe);
    }

    @Test
    void listar_ShouldReturnAllInfoCards() throws Exception {
        List<InfoCard> infoCardList = Arrays.asList(infoCard);
        when(repository.findAll()).thenReturn(infoCardList);

        mockMvc.perform(get("/api/info-cards"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].pk_info_card").value(1))
                .andExpect(jsonPath("$[0].title").value("Card 1"));

        verify(repository, times(1)).findAll();
    }

    @Test
    void criar_ShouldReturnCreatedInfoCard() throws Exception {
        when(repository.save(any(InfoCard.class))).thenReturn(infoCard);

        mockMvc.perform(post("/api/info-cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(infoCard)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk_info_card").value(1))
                .andExpect(jsonPath("$.title").value("Card 1"));

        verify(repository, times(1)).save(any(InfoCard.class));
    }

    @Test
    void atualizar_ShouldReturnUpdatedInfoCard() throws Exception {
        InfoCard updated = new InfoCard();
        updated.setTitle("Card atualizado");

        when(repository.findById(1)).thenReturn(Optional.of(infoCard));
        when(repository.save(any(InfoCard.class))).thenReturn(infoCard);

        mockMvc.perform(put("/api/info-cards/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk());

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(any(InfoCard.class));
    }

    @Test
    void atualizar_WhenNotFound_ShouldThrowException() throws Exception {
        InfoCard updated = new InfoCard();
        updated.setTitle("Card atualizado");

        when(repository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/info-cards/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().is5xxServerError());

        verify(repository, times(1)).findById(1);
        verify(repository, never()).save(any(InfoCard.class));
    }

    @Test
    void deletar_ShouldDeleteInfoCard() throws Exception {
        doNothing().when(repository).deleteById(1);

        mockMvc.perform(delete("/api/info-cards/1"))
                .andExpect(status().isOk());

        verify(repository, times(1)).deleteById(1);
    }
}
