package com.vitoria.portfolio_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitoria.portfolio_api.entity.Tag;
import com.vitoria.portfolio_api.repository.TagRepository;
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

@WebMvcTest(TagController.class)
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TagRepository repository;

    private Tag tag;

    @BeforeEach
    void setUp() {
        tag = new Tag();
        tag.setPk_tag(1);
        tag.setName("spring");
    }

    @Test
    void listar_ShouldReturnAllTags() throws Exception {
        List<Tag> tagList = Arrays.asList(tag);
        when(repository.findAll()).thenReturn(tagList);

        mockMvc.perform(get("/api/tags"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].pk_tag").value(1))
                .andExpect(jsonPath("$[0].name").value("spring"));

        verify(repository, times(1)).findAll();
    }

    @Test
    void criar_ShouldReturnCreatedTag() throws Exception {
        when(repository.save(any(Tag.class))).thenReturn(tag);

        mockMvc.perform(post("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tag)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk_tag").value(1))
                .andExpect(jsonPath("$.name").value("spring"));

        verify(repository, times(1)).save(any(Tag.class));
    }

    @Test
    void atualizar_ShouldReturnUpdatedTag() throws Exception {
        Tag updated = new Tag();
        updated.setName("java");

        when(repository.findById(1)).thenReturn(Optional.of(tag));
        when(repository.save(any(Tag.class))).thenReturn(tag);

        mockMvc.perform(put("/api/tags/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk());

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(any(Tag.class));
    }

    @Test
    void atualizar_WhenNotFound_ShouldThrowException() throws Exception {
        Tag updated = new Tag();
        updated.setName("java");

        when(repository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/tags/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().is5xxServerError());

        verify(repository, times(1)).findById(1);
        verify(repository, never()).save(any(Tag.class));
    }

    @Test
    void deletar_ShouldDeleteTag() throws Exception {
        doNothing().when(repository).deleteById(1);

        mockMvc.perform(delete("/api/tags/1"))
                .andExpect(status().isOk());

        verify(repository, times(1)).deleteById(1);
    }
}
