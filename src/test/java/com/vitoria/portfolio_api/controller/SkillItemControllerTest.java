package com.vitoria.portfolio_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitoria.portfolio_api.entity.Skill;
import com.vitoria.portfolio_api.entity.SkillCategory;
import com.vitoria.portfolio_api.repository.SkillRepository;
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

@WebMvcTest(SkillItemController.class)
class SkillItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SkillRepository repository;

    private Skill skill;

    @BeforeEach
    void setUp() {
        SkillCategory category = new SkillCategory();
        category.setPk_category(1);
        category.setName("Backend");

        skill = new Skill();
        skill.setPk_skill(1);
        skill.setName("Java");
        skill.setPercentage(90);
        skill.setCategory(category);
    }

    @Test
    void listar_ShouldReturnAllSkills() throws Exception {
        List<Skill> skillList = Arrays.asList(skill);
        when(repository.findAll()).thenReturn(skillList);

        mockMvc.perform(get("/api/skills-detalhes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].pk_skill").value(1))
                .andExpect(jsonPath("$[0].name").value("Java"));

        verify(repository, times(1)).findAll();
    }

    @Test
    void criar_ShouldReturnCreatedSkill() throws Exception {
        when(repository.save(any(Skill.class))).thenReturn(skill);

        mockMvc.perform(post("/api/skills-detalhes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(skill)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk_skill").value(1))
                .andExpect(jsonPath("$.name").value("Java"));

        verify(repository, times(1)).save(any(Skill.class));
    }

    @Test
    void atualizar_ShouldReturnUpdatedSkill() throws Exception {
        Skill updated = new Skill();
        updated.setName("Spring Boot");
        updated.setPercentage(85);

        when(repository.findById(1)).thenReturn(Optional.of(skill));
        when(repository.save(any(Skill.class))).thenReturn(skill);

        mockMvc.perform(put("/api/skills-detalhes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk());

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(any(Skill.class));
    }

    @Test
    void atualizar_WhenNotFound_ShouldThrowException() throws Exception {
        Skill updated = new Skill();
        updated.setName("Spring Boot");

        when(repository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/skills-detalhes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().is5xxServerError());

        verify(repository, times(1)).findById(1);
        verify(repository, never()).save(any(Skill.class));
    }

    @Test
    void deletar_ShouldDeleteSkill() throws Exception {
        doNothing().when(repository).deleteById(1);

        mockMvc.perform(delete("/api/skills-detalhes/1"))
                .andExpect(status().isOk());

        verify(repository, times(1)).deleteById(1);
    }
}
