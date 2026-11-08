package com.vitoria.portfolio_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitoria.portfolio_api.entity.Project;
import com.vitoria.portfolio_api.entity.ProjectHighlight;
import com.vitoria.portfolio_api.repository.ProjectHighlightRepository;
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

@WebMvcTest(ProjectHighlightController.class)
class ProjectHighlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProjectHighlightRepository repository;

    private ProjectHighlight highlight;

    @BeforeEach
    void setUp() {
        Project project = new Project();
        project.setPk_project(1);
        project.setTitle("My Project");

        highlight = new ProjectHighlight();
        highlight.setPk_highlight(1);
        highlight.setDescription("Destaque do projeto");
        highlight.setProject(project);
    }

    @Test
    void listar_ShouldReturnAllHighlights() throws Exception {
        List<ProjectHighlight> highlightList = Arrays.asList(highlight);
        when(repository.findAll()).thenReturn(highlightList);

        mockMvc.perform(get("/api/destaques"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].pk_highlight").value(1))
                .andExpect(jsonPath("$[0].description").value("Destaque do projeto"));

        verify(repository, times(1)).findAll();
    }

    @Test
    void criar_ShouldReturnCreatedHighlight() throws Exception {
        when(repository.save(any(ProjectHighlight.class))).thenReturn(highlight);

        mockMvc.perform(post("/api/destaques")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(highlight)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk_highlight").value(1));

        verify(repository, times(1)).save(any(ProjectHighlight.class));
    }

    @Test
    void atualizar_ShouldReturnUpdatedHighlight() throws Exception {
        ProjectHighlight updated = new ProjectHighlight();
        updated.setDescription("Destaque atualizado");

        when(repository.findById(1)).thenReturn(Optional.of(highlight));
        when(repository.save(any(ProjectHighlight.class))).thenReturn(highlight);

        mockMvc.perform(put("/api/destaques/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk());

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(any(ProjectHighlight.class));
    }

    @Test
    void atualizar_WhenNotFound_ShouldThrowException() throws Exception {
        ProjectHighlight updated = new ProjectHighlight();
        updated.setDescription("Destaque atualizado");

        when(repository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/destaques/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().is5xxServerError());

        verify(repository, times(1)).findById(1);
        verify(repository, never()).save(any(ProjectHighlight.class));
    }

    @Test
    void deletar_ShouldDeleteHighlight() throws Exception {
        doNothing().when(repository).deleteById(1);

        mockMvc.perform(delete("/api/destaques/1"))
                .andExpect(status().isOk());

        verify(repository, times(1)).deleteById(1);
    }
}
