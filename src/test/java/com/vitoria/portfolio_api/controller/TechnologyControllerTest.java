package com.vitoria.portfolio_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitoria.portfolio_api.entity.TechCategory;
import com.vitoria.portfolio_api.entity.Technology;
import com.vitoria.portfolio_api.repository.TechnologyRepository;
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

@WebMvcTest(TechnologyController.class)
class TechnologyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TechnologyRepository repository;

    private Technology technology;

    @BeforeEach
    void setUp() {
        TechCategory category = new TechCategory();
        category.setPk_category(1);
        category.setCategory_name("Backend");

        technology = new Technology();
        technology.setPk_technology(1);
        technology.setName("Java");
        technology.setCategory(category);
    }

    @Test
    void listar_ShouldReturnAllTechnologies() throws Exception {
        List<Technology> technologyList = Arrays.asList(technology);
        when(repository.findAll()).thenReturn(technologyList);

        mockMvc.perform(get("/api/tecnologias"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].pk_technology").value(1))
                .andExpect(jsonPath("$[0].name").value("Java"));

        verify(repository, times(1)).findAll();
    }

    @Test
    void criar_ShouldReturnCreatedTechnology() throws Exception {
        when(repository.save(any(Technology.class))).thenReturn(technology);

        mockMvc.perform(post("/api/tecnologias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(technology)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk_technology").value(1))
                .andExpect(jsonPath("$.name").value("Java"));

        verify(repository, times(1)).save(any(Technology.class));
    }

    @Test
    void atualizar_ShouldReturnUpdatedTechnology() throws Exception {
        Technology updated = new Technology();
        updated.setName("Spring Boot");

        when(repository.findById(1)).thenReturn(Optional.of(technology));
        when(repository.save(any(Technology.class))).thenReturn(technology);

        mockMvc.perform(put("/api/tecnologias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk());

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(any(Technology.class));
    }

    @Test
    void atualizar_WhenNotFound_ShouldThrowException() throws Exception {
        Technology updated = new Technology();
        updated.setName("Spring Boot");

        when(repository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/tecnologias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().is5xxServerError());

        verify(repository, times(1)).findById(1);
        verify(repository, never()).save(any(Technology.class));
    }

    @Test
    void deletar_ShouldDeleteTechnology() throws Exception {
        doNothing().when(repository).deleteById(1);

        mockMvc.perform(delete("/api/tecnologias/1"))
                .andExpect(status().isOk());

        verify(repository, times(1)).deleteById(1);
    }
}
