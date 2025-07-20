
package com.openfinance.service;

import com.openfinance.dto.request.CreateConsentRequest;
import com.openfinance.dto.response.ConsentResponse;
import com.openfinance.dto.response.RevokedConsentResponse;
import com.openfinance.model.Consent;
import com.openfinance.model.RevokedConsent;
import com.openfinance.repository.ConsentRepository;
import com.openfinance.repository.RevokedConsentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ConsentServiceTest {
    private ConsentRepository consentRepository;
    private RevokedConsentRepository revokedConsentRepository;
    private ConsentService consentService;

    @BeforeEach
    void setup()
    {
        consentRepository = mock(ConsentRepository.class);
        revokedConsentRepository = mock(RevokedConsentRepository.class);
        consentService = new ConsentService(consentRepository, revokedConsentRepository);
    }

    @Test
    void testCreateConsent_withValidData_shouldSaveAndReturnResponse()
    {
        CreateConsentRequest request = new CreateConsentRequest();
        request.setUserId("u1");

        when(consentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ConsentResponse response = consentService.createConsent("digio", request);

        assertEquals("u1", response.getUserId());
        assertTrue(response.isActive());
        assertNotNull(response.getCreatedAt());
        assertTrue(response.getId()
                .startsWith("urn:digio:"));
        verify(consentRepository).save(any());
    }

    @Test
    void testCreateConsent_withBlankUserId_shouldThrow()
    {
        CreateConsentRequest request = new CreateConsentRequest();
        request.setUserId(" ");

        assertThrows(Exception.class, () -> {
            consentService.createConsent("digio", request);
        });
    }

    @Test
    void testGetAllConsents_shouldReturnMappedList()
    {
        Consent c = new Consent();
        c.setId("id1");
        c.setUserId("user");
        c.setActive(true);
        c.setCreatedAt(LocalDateTime.now());

        when(consentRepository.findAll()).thenReturn(List.of(c));

        List<ConsentResponse> list = consentService.getAllConsents();

        assertEquals(1, list.size());
        assertEquals("user", list.get(0)
                .getUserId());
    }

    @Test
    void testGetActiveConsents_shouldReturnMappedList()
    {
        Consent c = new Consent();
        c.setId("id1");
        c.setUserId("user");
        c.setActive(true);
        c.setCreatedAt(LocalDateTime.now());

        when(consentRepository.findByActiveTrue()).thenReturn(List.of(c));

        List<ConsentResponse> list = consentService.getActiveConsents();

        assertEquals(1, list.size());
        assertEquals("user", list.get(0)
                .getUserId());
    }

    @Test
    void testRevokeConsent_shouldUpdateConsentAndSaveToMongo()
    {
        Consent c = new Consent();
        c.setId("urn:digio:id");
        c.setUserId("u1");
        c.setActive(true);
        c.setCreatedAt(LocalDateTime.now());

        when(consentRepository.findById("urn:digio:id")).thenReturn(Optional.of(c));

        consentService.revokeConsent("urn:digio:id");

        assertFalse(c.isActive());
        assertNotNull(c.getRevokedAt());

        verify(consentRepository).save(c);
        verify(revokedConsentRepository).save(any());
    }

    @Test
    void testRevokeConsent_notFound_shouldThrow()
    {
        when(consentRepository.findById("invalid")).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> {
            consentService.revokeConsent("invalid");
        });
    }

    @Test
    void testGetRevokedConsents_shouldReturnMappedList()
    {
        RevokedConsent revoked = new RevokedConsent();
        revoked.setId("r1");
        revoked.setUserId("u1");
        revoked.setCreatedAt(LocalDateTime.now());
        revoked.setRevokedAt(LocalDateTime.now());

        when(revokedConsentRepository.findAll()).thenReturn(List.of(revoked));

        List<RevokedConsentResponse> list = consentService.getRevokedConsents();

        assertEquals(1, list.size());
        assertEquals("u1", list.get(0)
                .getUserId());
    }
}
