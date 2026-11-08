package com.vitoria.portfolio_api.controller;

import com.vitoria.portfolio_api.service.ImageStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UploadController.class)
class UploadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageStorageService storageService;

    @Test
    void upload_ShouldReturnUrl() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "foto.png", "image/png", "fake-image".getBytes());

        when(storageService.uploadImage(any())).thenReturn("https://example.com/img.png");

        mockMvc.perform(multipart("/api/upload").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("https://example.com/img.png"));

        verify(storageService, times(1)).uploadImage(any());
    }

    @Test
    void upload_WhenInvalidFile_ShouldReturnBadRequest() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "doc.pdf", "application/pdf", "fake".getBytes());

        when(storageService.uploadImage(any()))
                .thenThrow(new IllegalArgumentException("Tipo de arquivo não permitido"));

        mockMvc.perform(multipart("/api/upload").file(file))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void upload_WhenStorageFails_ShouldReturnServerError() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "foto.png", "image/png", "fake-image".getBytes());

        when(storageService.uploadImage(any())).thenThrow(new IOException("connection refused"));

        mockMvc.perform(multipart("/api/upload").file(file))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").exists());
    }
}
