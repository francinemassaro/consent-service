package com.openfinance.service;

import com.openfinance.model.Consent;
import com.openfinance.model.RevokedConsent;
import com.openfinance.repository.ConsentRepository;
import com.openfinance.repository.RevokedConsentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConsentService {
    private ConsentRepository repository;
    private final RevokedConsentRepository revokedConsentRepository;

    public ConsentService(ConsentRepository repository, RevokedConsentRepository revokedConsentRepository)
    {
        this.repository = repository;
        this.revokedConsentRepository = revokedConsentRepository;
    }

    public List<Consent> getAllConsents()
    {
        return repository.findAll();
    }

    public void createConsent(Consent consent)
    {
        repository.save(consent);
    }

    public void revokeConsent(String id)
    {
        repository.findById(id).ifPresent(c -> {
            c.setActive(false);
            c.setRevokedAt(LocalDateTime.now());
            repository.save(c);

            RevokedConsent revoked = new RevokedConsent();
            revoked.setId(c.getId());
            revoked.setUserId(c.getUserId());
            revoked.setCreatedAt(c.getCreatedAt());
            revoked.setRevokedAt(c.getRevokedAt());

            revokedConsentRepository.save(revoked);
        });
    }

    public List<Consent> getActiveConsents()
    {
        return repository.findByActiveTrue();
    }

    public List<RevokedConsent> getRevokedConsents() {
        return revokedConsentRepository.findAll();
    }
}
