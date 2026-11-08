package com.vitoria.portfolio_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitoria.portfolio_api.entity.Post;
import com.vitoria.portfolio_api.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostRepository repository;

    private Post post;

    @BeforeEach
    void setUp() {
        post = new Post();
        post.setPk_post(1);
        post.setPlatform("LinkedIn");
        post.setTitle("My First Post");
        post.setDescription("This is a test post");
        post.setPublish_date(LocalDate.of(2023, 6, 15));
        post.setUrl_link("https://linkedin.com/post/1");
    }

    @Test
    void listar_ShouldReturnAllPosts() throws Exception {
        List<Post> postList = Arrays.asList(post);
        when(repository.findAll()).thenReturn(postList);

        mockMvc.perform(get("/api/publicacoes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].pk_post").value(1))
                .andExpect(jsonPath("$[0].title").value("My First Post"));

        verify(repository, times(1)).findAll();
    }

    @Test
    void criar_ShouldReturnCreatedPost() throws Exception {
        when(repository.save(any(Post.class))).thenReturn(post);

        mockMvc.perform(post("/api/publicacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk_post").value(1))
                .andExpect(jsonPath("$.title").value("My First Post"));

        verify(repository, times(1)).save(any(Post.class));
    }

    @Test
    void atualizar_ShouldReturnUpdatedPost() throws Exception {
        Post updated = new Post();
        updated.setTitle("Updated Title");
        updated.setPlatform("Twitter");

        when(repository.findById(1)).thenReturn(Optional.of(post));
        when(repository.save(any(Post.class))).thenReturn(post);

        mockMvc.perform(put("/api/publicacoes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk());

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(any(Post.class));
    }

    @Test
    void atualizar_WhenNotFound_ShouldThrowException() throws Exception {
        Post updated = new Post();
        updated.setTitle("Updated Title");

        when(repository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/publicacoes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().is5xxServerError());

        verify(repository, times(1)).findById(1);
        verify(repository, never()).save(any(Post.class));
    }

    @Test
    void deletar_ShouldDeletePost() throws Exception {
        doNothing().when(repository).deleteById(1);

        mockMvc.perform(delete("/api/publicacoes/1"))
                .andExpect(status().isOk());

        verify(repository, times(1)).deleteById(1);
    }
}
