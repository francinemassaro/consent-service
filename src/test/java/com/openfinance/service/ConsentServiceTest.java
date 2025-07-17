package com.openfinance.service;

import com.openfinance.model.Consent;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsentServiceTest {
    @Test
    public void testCreateAndRevokeConsent() {
        ConsentService service = new ConsentService();
        Consent c = new Consent();
        c.setId("c1");
        c.setUserId("u1");
        c.setActive(true);
        c.setCreatedAt(LocalDateTime.now());

        service.createConsent(c);
        assertEquals(1, service.getAllConsents().size());

        service.revokeConsent("c1");
        Consent revoked = service.getAllConsents().get(0);
        assertFalse(revoked.isActive());
        assertNotNull(revoked.getRevokedAt());
    }

    @Test
    public void testGetActiveConsents() {
        ConsentService service = new ConsentService();

        Consent active = new Consent();
        active.setId("a1");
        active.setUserId("u1");
        active.setActive(true);
        active.setCreatedAt(LocalDateTime.now());

        Consent inactive = new Consent();
        inactive.setId("a2");
        inactive.setUserId("u2");
        inactive.setActive(false);
        inactive.setCreatedAt(LocalDateTime.now());

        service.createConsent(active);
        service.createConsent(inactive);

        List<Consent> activeConsents = service.getActiveConsents();
        assertEquals(1, activeConsents.size());
        assertTrue(activeConsents.get(0).isActive());
    }
}