package com.vitoria.portfolio_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitoria.portfolio_api.entity.Post;
import com.vitoria.portfolio_api.entity.PostTag;
import com.vitoria.portfolio_api.entity.Tag;
import com.vitoria.portfolio_api.repository.PostTagRepository;
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

@WebMvcTest(PostTagController.class)
class PostTagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostTagRepository repository;

    private PostTag postTag;

    @BeforeEach
    void setUp() {
        Post post = new Post();
        post.setPk_post(1);
        post.setTitle("My First Post");

        Tag tag = new Tag();
        tag.setPk_tag(1);
        tag.setName("spring");

        postTag = new PostTag();
        postTag.setPk_post_tag(1);
        postTag.setPost(post);
        postTag.setTag(tag);
    }

    @Test
    void listar_ShouldReturnAllPostTags() throws Exception {
        List<PostTag> postTagList = Arrays.asList(postTag);
        when(repository.findAll()).thenReturn(postTagList);

        mockMvc.perform(get("/api/post-tags"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].pk_post_tag").value(1));

        verify(repository, times(1)).findAll();
    }

    @Test
    void criar_ShouldReturnCreatedPostTag() throws Exception {
        when(repository.save(any(PostTag.class))).thenReturn(postTag);

        mockMvc.perform(post("/api/post-tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postTag)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk_post_tag").value(1));

        verify(repository, times(1)).save(any(PostTag.class));
    }

    @Test
    void atualizar_ShouldReturnUpdatedPostTag() throws Exception {
        Tag updatedTag = new Tag();
        updatedTag.setPk_tag(2);
        updatedTag.setName("java");

        PostTag updated = new PostTag();
        updated.setTag(updatedTag);

        when(repository.findById(1)).thenReturn(Optional.of(postTag));
        when(repository.save(any(PostTag.class))).thenReturn(postTag);

        mockMvc.perform(put("/api/post-tags/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk());

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(any(PostTag.class));
    }

    @Test
    void atualizar_WhenNotFound_ShouldThrowException() throws Exception {
        PostTag updated = new PostTag();
        updated.setTag(new Tag());

        when(repository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/post-tags/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().is5xxServerError());

        verify(repository, times(1)).findById(1);
        verify(repository, never()).save(any(PostTag.class));
    }

    @Test
    void deletar_ShouldDeletePostTag() throws Exception {
        doNothing().when(repository).deleteById(1);

        mockMvc.perform(delete("/api/post-tags/1"))
                .andExpect(status().isOk());

        verify(repository, times(1)).deleteById(1);
    }
}
