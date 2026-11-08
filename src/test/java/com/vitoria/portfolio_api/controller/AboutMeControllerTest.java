package com.vitoria.portfolio_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitoria.portfolio_api.entity.AboutMe;
import com.vitoria.portfolio_api.repository.AboutMeRepository;
import com.vitoria.portfolio_api.repository.ProjectRepository;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AboutMeController.class)
class AboutMeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AboutMeRepository repository;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private TechnologyRepository technologyRepository;

    private AboutMe aboutMe;

    @BeforeEach
    void setUp() {
        aboutMe = new AboutMe();
        aboutMe.setPk_about_me(1);
        aboutMe.setDescription("Test description");
        aboutMe.setNumber_projects(10);
        aboutMe.setNumber_technologies(5);
    }

    @Test
    void listar_ShouldReturnAllAboutMe() throws Exception {
        List<AboutMe> aboutMeList = Arrays.asList(aboutMe);
        when(repository.findAll()).thenReturn(aboutMeList);

        mockMvc.perform(get("/api/sobre-mim"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].pk_about_me").value(1))
                .andExpect(jsonPath("$[0].description").value("Test description"));

        verify(repository, times(1)).findAll();
    }

    @Test
    void criar_ShouldReturnCreatedAboutMe() throws Exception {
        when(repository.save(any(AboutMe.class))).thenReturn(aboutMe);

        mockMvc.perform(post("/api/sobre-mim")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(aboutMe)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk_about_me").value(1))
                .andExpect(jsonPath("$.description").value("Test description"));

        verify(repository, times(1)).save(any(AboutMe.class));
    }

    @Test
    void atualizar_ShouldReturnUpdatedAboutMe() throws Exception {
        AboutMe updated = new AboutMe();
        updated.setDescription("Updated description");
        updated.setNumber_projects(20);
        updated.setNumber_technologies(10);

        when(repository.findById(1)).thenReturn(Optional.of(aboutMe));
        when(repository.save(any(AboutMe.class))).thenReturn(aboutMe);

        mockMvc.perform(put("/api/sobre-mim/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk());

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(any(AboutMe.class));
    }

    @Test
    void atualizar_WhenNotFound_ShouldThrowException() throws Exception {
        AboutMe updated = new AboutMe();
        updated.setDescription("Updated description");

        when(repository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/sobre-mim/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().is5xxServerError());

        verify(repository, times(1)).findById(1);
        verify(repository, never()).save(any(AboutMe.class));
    }

    @Test
    void deletar_ShouldDeleteAboutMe() throws Exception {
        doNothing().when(repository).deleteById(1);

        mockMvc.perform(delete("/api/sobre-mim/1"))
                .andExpect(status().isOk());

        verify(repository, times(1)).deleteById(1);
    }
}
