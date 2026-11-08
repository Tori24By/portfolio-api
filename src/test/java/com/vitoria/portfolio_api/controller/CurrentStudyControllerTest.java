package com.vitoria.portfolio_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitoria.portfolio_api.entity.CurrentStudy;
import com.vitoria.portfolio_api.repository.CurrentStudyRepository;
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

@WebMvcTest(CurrentStudyController.class)
class CurrentStudyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CurrentStudyRepository repository;

    private CurrentStudy currentStudy;

    @BeforeEach
    void setUp() {
        currentStudy = new CurrentStudy();
        currentStudy.setPk_study(1);
        currentStudy.setCategory("Programming");
        currentStudy.setTitle("Spring Boot");
        currentStudy.setDescription("Learning Spring Boot framework");
        currentStudy.setPercentage_progress(75);
    }

    @Test
    void listar_ShouldReturnAllCurrentStudies() throws Exception {
        List<CurrentStudy> studyList = Arrays.asList(currentStudy);
        when(repository.findAll()).thenReturn(studyList);

        mockMvc.perform(get("/api/estudos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].pk_study").value(1))
                .andExpect(jsonPath("$[0].title").value("Spring Boot"));

        verify(repository, times(1)).findAll();
    }

    @Test
    void criar_ShouldReturnCreatedCurrentStudy() throws Exception {
        when(repository.save(any(CurrentStudy.class))).thenReturn(currentStudy);

        mockMvc.perform(post("/api/estudos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(currentStudy)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk_study").value(1))
                .andExpect(jsonPath("$.title").value("Spring Boot"));

        verify(repository, times(1)).save(any(CurrentStudy.class));
    }

    @Test
    void atualizar_ShouldReturnUpdatedCurrentStudy() throws Exception {
        CurrentStudy updated = new CurrentStudy();
        updated.setTitle("Advanced Spring");
        updated.setPercentage_progress(90);

        when(repository.findById(1)).thenReturn(Optional.of(currentStudy));
        when(repository.save(any(CurrentStudy.class))).thenReturn(currentStudy);

        mockMvc.perform(put("/api/estudos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk());

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(any(CurrentStudy.class));
    }

    @Test
    void atualizar_WhenNotFound_ShouldThrowException() throws Exception {
        CurrentStudy updated = new CurrentStudy();
        updated.setTitle("Advanced Spring");

        when(repository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/estudos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().is5xxServerError());

        verify(repository, times(1)).findById(1);
        verify(repository, never()).save(any(CurrentStudy.class));
    }

    @Test
    void deletar_ShouldDeleteCurrentStudy() throws Exception {
        doNothing().when(repository).deleteById(1);

        mockMvc.perform(delete("/api/estudos/1"))
                .andExpect(status().isOk());

        verify(repository, times(1)).deleteById(1);
    }
}
