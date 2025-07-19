package com.openfinance.controller;

import com.openfinance.model.Consent;
import com.openfinance.service.ConsentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/consents")
public class ConsentController {

    private final ConsentService consentService;

    public ConsentController(ConsentService consentService) {
        this.consentService = consentService;
    }

    @GetMapping
    public List<Consent> getAllConsents() {
        return consentService.getAllConsents();
    }

    @PostMapping
    public void createConsent(@RequestBody Consent consent) {
        consentService.createConsent(consent);
    }

    @PatchMapping("/{id}/revoke")
    public void revokeConsent(@PathVariable String id) {
        consentService.revokeConsent(id);
    }

    @GetMapping("/active")
    public List<Consent> getActiveConsents() {
        return consentService.getActiveConsents();
    }
}

