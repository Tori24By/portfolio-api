package com.vitoria.portfolio_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitoria.portfolio_api.entity.Certificate;
import com.vitoria.portfolio_api.repository.CertificateRepository;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CertificateController.class)
class CertificateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CertificateRepository repository;

    private Certificate certificate;

    @BeforeEach
    void setUp() {
        certificate = new Certificate();
        certificate.setPk_certificate(1);
        certificate.setCategory("Java");
        certificate.setTitle("Java Certification");
        certificate.setIssuer("Oracle");
        certificate.setStart_date(LocalDate.of(2023, 1, 1));
        certificate.setEnd_date(LocalDate.of(2023, 12, 31));
        certificate.setImage_url("http://example.com/image.png");
        certificate.setHours(40);
        certificate.setCredential_url("http://example.com/credential");
    }

    @Test
    void listarTodos_ShouldReturnAllCertificates() throws Exception {
        List<Certificate> certificateList = Arrays.asList(certificate);
        when(repository.findAll()).thenReturn(certificateList);

        mockMvc.perform(get("/api/certificados"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].pk_certificate").value(1))
                .andExpect(jsonPath("$[0].title").value("Java Certification"));

        verify(repository, times(1)).findAll();
    }

    @Test
    void criar_ShouldReturnCreatedCertificate() throws Exception {
        when(repository.save(any(Certificate.class))).thenReturn(certificate);

        mockMvc.perform(post("/api/certificados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(certificate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk_certificate").value(1))
                .andExpect(jsonPath("$.title").value("Java Certification"));

        verify(repository, times(1)).save(any(Certificate.class));
    }

    @Test
    void atualizar_ShouldReturnUpdatedCertificate() throws Exception {
        Certificate updated = new Certificate();
        updated.setTitle("Updated Title");
        updated.setCategory("Updated Category");

        when(repository.findById(1)).thenReturn(Optional.of(certificate));
        when(repository.save(any(Certificate.class))).thenReturn(certificate);

        mockMvc.perform(put("/api/certificados/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk());

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(any(Certificate.class));
    }

    @Test
    void atualizar_WhenNotFound_ShouldThrowException() throws Exception {
        Certificate updated = new Certificate();
        updated.setTitle("Updated Title");

        when(repository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/certificados/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().is5xxServerError());

        verify(repository, times(1)).findById(1);
        verify(repository, never()).save(any(Certificate.class));
    }

    @Test
    void deletar_ShouldDeleteCertificate() throws Exception {
        doNothing().when(repository).deleteById(1);

        mockMvc.perform(delete("/api/certificados/1"))
                .andExpect(status().isOk());

        verify(repository, times(1)).deleteById(1);
    }
}
