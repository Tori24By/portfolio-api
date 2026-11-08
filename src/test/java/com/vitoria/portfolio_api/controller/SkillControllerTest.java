package com.vitoria.portfolio_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitoria.portfolio_api.entity.SkillCategory;
import com.vitoria.portfolio_api.repository.SkillCategoryRepository;
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

@WebMvcTest(SkillController.class)
class SkillControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SkillCategoryRepository repository;

    private SkillCategory skillCategory;

    @BeforeEach
    void setUp() {
        skillCategory = new SkillCategory();
        skillCategory.setPk_category(1);
        skillCategory.setName("Backend");
    }

    @Test
    void listarCategorias_ShouldReturnAllSkillCategories() throws Exception {
        List<SkillCategory> categoryList = Arrays.asList(skillCategory);
        when(repository.findAll()).thenReturn(categoryList);

        mockMvc.perform(get("/api/habilidades"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].pk_category").value(1))
                .andExpect(jsonPath("$[0].name").value("Backend"));

        verify(repository, times(1)).findAll();
    }

    @Test
    void criarCategoria_ShouldReturnCreatedSkillCategory() throws Exception {
        when(repository.save(any(SkillCategory.class))).thenReturn(skillCategory);

        mockMvc.perform(post("/api/habilidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(skillCategory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk_category").value(1))
                .andExpect(jsonPath("$.name").value("Backend"));

        verify(repository, times(1)).save(any(SkillCategory.class));
    }

    @Test
    void atualizar_ShouldReturnUpdatedSkillCategory() throws Exception {
        SkillCategory updated = new SkillCategory();
        updated.setName("Frontend");

        when(repository.findById(1)).thenReturn(Optional.of(skillCategory));
        when(repository.save(any(SkillCategory.class))).thenReturn(skillCategory);

        mockMvc.perform(put("/api/habilidades/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk());

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(any(SkillCategory.class));
    }

    @Test
    void atualizar_WhenNotFound_ShouldThrowException() throws Exception {
        SkillCategory updated = new SkillCategory();
        updated.setName("Frontend");

        when(repository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/habilidades/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().is5xxServerError());

        verify(repository, times(1)).findById(1);
        verify(repository, never()).save(any(SkillCategory.class));
    }

    @Test
    void deletar_ShouldDeleteSkillCategory() throws Exception {
        doNothing().when(repository).deleteById(1);

        mockMvc.perform(delete("/api/habilidades/1"))
                .andExpect(status().isOk());

        verify(repository, times(1)).deleteById(1);
    }
}
