package com.openfinance.service;

import com.openfinance.model.Consent;
import com.openfinance.repository.ConsentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsentService {
    private ConsentRepository repository;

    public ConsentService(ConsentRepository repository) {
        this.repository = repository;
    }

    public List<Consent> getAllConsents() {
        return repository.findAll();
    }

    public void createConsent(Consent consent) {
        repository.save(consent);
    }

    public void revokeConsent(String id) {
        repository.findById(id).ifPresent(c -> {
            c.setActive(false);
            c.setRevokedAt(LocalDateTime.now());
            repository.save(c);
        });
    }

    public List<Consent> getActiveConsents() {
        return repository.findAll().stream()
                .filter(Consent::isActive)
                .collect(Collectors.toList());
    }
}
