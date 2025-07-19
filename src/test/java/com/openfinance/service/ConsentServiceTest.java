package com.openfinance.service;

import com.openfinance.model.Consent;
import com.openfinance.model.RevokedConsent;
import com.openfinance.repository.ConsentRepository;
import com.openfinance.repository.RevokedConsentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ConsentServiceTest {
    private ConsentRepository consentRepository;
    private RevokedConsentRepository revokedConsentRepository;
    private ConsentService consentService;

    @BeforeEach
    void setup() {
        consentRepository = mock(ConsentRepository.class);
        revokedConsentRepository = mock(RevokedConsentRepository.class);
        consentService = new ConsentService(consentRepository, revokedConsentRepository);
    }

    @Test
    void testCreateConsent() {
        Consent consent = new Consent();
        consent.setId("c1");
        consent.setUserId("u1");
        consent.setActive(true);
        consent.setCreatedAt(LocalDateTime.now());

        consentService.createConsent(consent);

        verify(consentRepository, times(1)).save(consent);
    }

    @Test
    void testGetActiveConsents() {
        Consent c1 = new Consent();
        c1.setId("a1");
        c1.setActive(true);
        Consent c2 = new Consent();
        c2.setId("a2");
        c2.setActive(false);

        when(consentRepository.findByActiveTrue()).thenReturn(List.of(c1));

        List<Consent> result = consentService.getActiveConsents();

        assertEquals(1, result.size());
        assertTrue(result.get(0).isActive());
    }

    @Test
    void testRevokeConsent_shouldSaveToMongo() {
        Consent c = new Consent();
        c.setId("c1");
        c.setUserId("u1");
        c.setActive(true);
        c.setCreatedAt(LocalDateTime.now());

        when(consentRepository.findById("c1")).thenReturn(Optional.of(c));

        consentService.revokeConsent("c1");

        assertFalse(c.isActive());
        assertNotNull(c.getRevokedAt());

        verify(consentRepository).save(c);

        ArgumentCaptor<RevokedConsent> captor = ArgumentCaptor.forClass(RevokedConsent.class);
        verify(revokedConsentRepository).save(captor.capture());

        RevokedConsent saved = captor.getValue();
        assertEquals("c1", saved.getId());
        assertEquals("u1", saved.getUserId());
    }

    @Test
    void testRevokeConsent_notFound() {
        when(consentRepository.findById("naoexiste")).thenReturn(Optional.empty());

        consentService.revokeConsent("naoexiste");

        verify(consentRepository, never()).save(any());
        verify(revokedConsentRepository, never()).save(any());
    }

    @Test
    void testGetAllConsents() {
        when(consentRepository.findAll()).thenReturn(List.of(new Consent(), new Consent()));
        List<Consent> list = consentService.getAllConsents();
        assertEquals(2, list.size());
    }

    @Test
    void testGetRevokedConsents() {
        when(revokedConsentRepository.findAll()).thenReturn(List.of(new RevokedConsent()));
        List<RevokedConsent> list = consentService.getRevokedConsents();
        assertEquals(1, list.size());
    }
}