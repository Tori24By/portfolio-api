package com.vitoria.portfolio_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitoria.portfolio_api.entity.Project;
import com.vitoria.portfolio_api.entity.ProjectTechnology;
import com.vitoria.portfolio_api.entity.Technology;
import com.vitoria.portfolio_api.repository.ProjectTechnologyRepository;
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

@WebMvcTest(ProjectTechnologyController.class)
class ProjectTechnologyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProjectTechnologyRepository repository;

    private ProjectTechnology projectTechnology;

    @BeforeEach
    void setUp() {
        Project project = new Project();
        project.setPk_project(1);
        project.setTitle("My Project");

        Technology technology = new Technology();
        technology.setPk_technology(1);
        technology.setName("Java");

        projectTechnology = new ProjectTechnology();
        projectTechnology.setPk_project_tech(1);
        projectTechnology.setProject(project);
        projectTechnology.setTechnology(technology);
    }

    @Test
    void listar_ShouldReturnAllProjectTechnologies() throws Exception {
        List<ProjectTechnology> projectTechnologyList = Arrays.asList(projectTechnology);
        when(repository.findAll()).thenReturn(projectTechnologyList);

        mockMvc.perform(get("/api/tecnologias-projeto"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].pk_project_tech").value(1));

        verify(repository, times(1)).findAll();
    }

    @Test
    void criar_ShouldReturnCreatedProjectTechnology() throws Exception {
        when(repository.save(any(ProjectTechnology.class))).thenReturn(projectTechnology);

        mockMvc.perform(post("/api/tecnologias-projeto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectTechnology)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk_project_tech").value(1));

        verify(repository, times(1)).save(any(ProjectTechnology.class));
    }

    @Test
    void atualizar_ShouldReturnUpdatedProjectTechnology() throws Exception {
        Technology updatedTechnology = new Technology();
        updatedTechnology.setPk_technology(2);
        updatedTechnology.setName("Spring Boot");

        ProjectTechnology updated = new ProjectTechnology();
        updated.setTechnology(updatedTechnology);

        when(repository.findById(1)).thenReturn(Optional.of(projectTechnology));
        when(repository.save(any(ProjectTechnology.class))).thenReturn(projectTechnology);

        mockMvc.perform(put("/api/tecnologias-projeto/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk());

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(any(ProjectTechnology.class));
    }

    @Test
    void atualizar_WhenNotFound_ShouldThrowException() throws Exception {
        ProjectTechnology updated = new ProjectTechnology();
        updated.setTechnology(new Technology());

        when(repository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/tecnologias-projeto/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().is5xxServerError());

        verify(repository, times(1)).findById(1);
        verify(repository, never()).save(any(ProjectTechnology.class));
    }

    @Test
    void deletar_ShouldDeleteProjectTechnology() throws Exception {
        doNothing().when(repository).deleteById(1);

        mockMvc.perform(delete("/api/tecnologias-projeto/1"))
                .andExpect(status().isOk());

        verify(repository, times(1)).deleteById(1);
    }
}
