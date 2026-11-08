package com.vitoria.portfolio_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitoria.portfolio_api.entity.TechCategory;
import com.vitoria.portfolio_api.repository.TechCategoryRepository;
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

@WebMvcTest(TechCategoryController.class)
class TechCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TechCategoryRepository repository;

    private TechCategory techCategory;

    @BeforeEach
    void setUp() {
        techCategory = new TechCategory();
        techCategory.setPk_category(1);
        techCategory.setCategory_name("Backend");
    }

    @Test
    void listar_ShouldReturnAllTechCategories() throws Exception {
        List<TechCategory> categoryList = Arrays.asList(techCategory);
        when(repository.findAll()).thenReturn(categoryList);

        mockMvc.perform(get("/api/categorias-tech"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].pk_category").value(1))
                .andExpect(jsonPath("$[0].category_name").value("Backend"));

        verify(repository, times(1)).findAll();
    }

    @Test
    void criar_ShouldReturnCreatedTechCategory() throws Exception {
        when(repository.save(any(TechCategory.class))).thenReturn(techCategory);

        mockMvc.perform(post("/api/categorias-tech")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(techCategory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk_category").value(1))
                .andExpect(jsonPath("$.category_name").value("Backend"));

        verify(repository, times(1)).save(any(TechCategory.class));
    }

    @Test
    void atualizar_ShouldReturnUpdatedTechCategory() throws Exception {
        TechCategory updated = new TechCategory();
        updated.setCategory_name("Frontend");

        when(repository.findById(1)).thenReturn(Optional.of(techCategory));
        when(repository.save(any(TechCategory.class))).thenReturn(techCategory);

        mockMvc.perform(put("/api/categorias-tech/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk());

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(any(TechCategory.class));
    }

    @Test
    void atualizar_WhenNotFound_ShouldThrowException() throws Exception {
        TechCategory updated = new TechCategory();
        updated.setCategory_name("Frontend");

        when(repository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/categorias-tech/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().is5xxServerError());

        verify(repository, times(1)).findById(1);
        verify(repository, never()).save(any(TechCategory.class));
    }

    @Test
    void deletar_ShouldDeleteTechCategory() throws Exception {
        doNothing().when(repository).deleteById(1);

        mockMvc.perform(delete("/api/categorias-tech/1"))
                .andExpect(status().isOk());

        verify(repository, times(1)).deleteById(1);
    }
}
