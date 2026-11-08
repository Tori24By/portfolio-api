package com.vitoria.portfolio_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitoria.portfolio_api.entity.Project;
import com.vitoria.portfolio_api.repository.ProjectRepository;
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

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProjectRepository repository;

    private Project project;

    @BeforeEach
    void setUp() {
        project = new Project();
        project.setPk_project(1);
        project.setTitle("My Project");
        project.setType("Web App");
        project.setAbout("A test project");
        project.setImage_url("http://example.com/image.png");
        project.setShort_description("Short desc");
        project.setFeatured(true);
        project.setLive_url("http://example.com");
        project.setRepo_url("http://github.com/repo");
    }

    @Test
    void listar_ShouldReturnAllProjects() throws Exception {
        List<Project> projectList = Arrays.asList(project);
        when(repository.findAll()).thenReturn(projectList);

        mockMvc.perform(get("/api/projetos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].pk_project").value(1))
                .andExpect(jsonPath("$[0].title").value("My Project"));

        verify(repository, times(1)).findAll();
    }

    @Test
    void criar_ShouldReturnCreatedProject() throws Exception {
        when(repository.save(any(Project.class))).thenReturn(project);

        mockMvc.perform(post("/api/projetos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk_project").value(1))
                .andExpect(jsonPath("$.title").value("My Project"));

        verify(repository, times(1)).save(any(Project.class));
    }

    @Test
    void atualizar_ShouldReturnUpdatedProject() throws Exception {
        Project updated = new Project();
        updated.setTitle("Updated Title");
        updated.setType("Mobile App");

        when(repository.findById(1)).thenReturn(Optional.of(project));
        when(repository.save(any(Project.class))).thenReturn(project);

        mockMvc.perform(put("/api/projetos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk());

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(any(Project.class));
    }

    @Test
    void atualizar_WhenNotFound_ShouldThrowException() throws Exception {
        Project updated = new Project();
        updated.setTitle("Updated Title");

        when(repository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/projetos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().is5xxServerError());

        verify(repository, times(1)).findById(1);
        verify(repository, never()).save(any(Project.class));
    }

    @Test
    void deletar_ShouldDeleteProject() throws Exception {
        doNothing().when(repository).deleteById(1);

        mockMvc.perform(delete("/api/projetos/1"))
                .andExpect(status().isOk());

        verify(repository, times(1)).deleteById(1);
    }
}
