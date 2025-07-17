package com.openfinance.controller;

import com.openfinance.model.Consent;
import com.openfinance.service.ConsentService;

import java.util.List;

public class ConsentController {
    private ConsentService service = new ConsentService();

    public List<Consent> getAllConsents() {
        return service.getAllConsents();
    }

    public void createConsent(Consent consent) {
        service.createConsent(consent);
    }

    public void revokeConsent(String id) {
        service.revokeConsent(id);
    }

    public List<Consent> getActiveConsents() {
        return service.getActiveConsents();
    }
}
