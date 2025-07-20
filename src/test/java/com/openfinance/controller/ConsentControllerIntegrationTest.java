
package com.openfinance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openfinance.dto.request.CreateConsentRequest;
import com.openfinance.model.Consent;
import com.openfinance.repository.ConsentRepository;
import com.openfinance.repository.RevokedConsentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("integration")
@SpringBootTest
@AutoConfigureMockMvc
class ConsentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ConsentRepository consentRepository;

    @Autowired
    private RevokedConsentRepository revokedConsentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        consentRepository.deleteAll();
        revokedConsentRepository.deleteAll();
    }

    @Test
    void shouldReturn400WhenUserIdIsMissing() throws Exception {
        CreateConsentRequest request = new CreateConsentRequest();
        request.setUserId("");

        mockMvc.perform(post("/consents/testbank")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCreateConsentSuccessfully() throws Exception {
        CreateConsentRequest request = new CreateConsentRequest();
        request.setUserId("usuario123");

        mockMvc.perform(post("/consents/testbank")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value("usuario123"))
                .andExpect(jsonPath("$.id").value(containsString("urn:testbank:")));
    }

    @Test
    void shouldReturnAllConsents() throws Exception {
        CreateConsentRequest request = new CreateConsentRequest();
        request.setUserId("user1");

        mockMvc.perform(post("/consents/banco1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/consents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void shouldReturnActiveConsents() throws Exception {
        CreateConsentRequest request = new CreateConsentRequest();
        request.setUserId("user2");

        mockMvc.perform(post("/consents/banco2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/consents/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void shouldRevokeConsent() throws Exception {
        CreateConsentRequest request = new CreateConsentRequest();
        request.setUserId("user3");

        String response = mockMvc.perform(post("/consents/testinst")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String id = objectMapper.readTree(response).get("id").asText();

        mockMvc.perform(patch("/consents/" + id + "/revoke"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/consents/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        mockMvc.perform(get("/consents/revoked"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void shouldReturn404WhenRevokingInvalidConsent() throws Exception {
        String fakeId = "urn:test:" + UUID.randomUUID();
        mockMvc.perform(patch("/consents/" + fakeId + "/revoke"))
                .andExpect(status().isNotFound());
    }
}
