package com.openfinance.service;

import com.openfinance.dto.request.CreateConsentRequest;
import com.openfinance.dto.response.ConsentResponse;
import com.openfinance.dto.response.RevokedConsentResponse;
import com.openfinance.enums.Institution;
import com.openfinance.model.Consent;
import com.openfinance.model.RevokedConsent;
import com.openfinance.repository.ConsentRepository;
import com.openfinance.repository.RevokedConsentRepository;
import com.openfinance.service.mapper.ConsentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ConsentServiceImpl implements ConsentService {
    private ConsentRepository repository;
    private final RevokedConsentRepository revokedConsentRepository;
    private static final Logger log = LoggerFactory.getLogger(ConsentServiceImpl.class);

    public ConsentServiceImpl(ConsentRepository repository, RevokedConsentRepository revokedConsentRepository)
    {
        this.repository = repository;
        this.revokedConsentRepository = revokedConsentRepository;
    }

    @Override
    public List<ConsentResponse> getAllConsents()
    {
        List<Consent> consentList = repository.findAll();

        log.info("Listagem de consentimentos no banco. Total: {}", consentList.size());
        return consentList.stream()
                .map(ConsentMapper::mapConsentToResponse)
                .toList();
    }

    @Override
    public ConsentResponse createConsent(String institutionId, CreateConsentRequest request) {
        if (!Institution.isValid(institutionId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Instituição inválida: " + institutionId);
        }

        if (request.getUserId() == null || request.getUserId().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo 'userId' é obrigatório.");
        }

        String id = "urn:" + institutionId + ":" + UUID.randomUUID();

        Consent consent = new Consent();
        consent.setId(id);
        consent.setUserId(request.getUserId());
        consent.setActive(true);
        consent.setCreatedAt(LocalDateTime.now());

        Consent saved = repository.save(consent);

        log.info("Consentimento salvo com sucesso no Postgres: id={}", consent.getId());
        return ConsentMapper.mapConsentToResponse(saved);
    }

    @Override
    public void revokeConsent(String id)
    {
        Consent consent = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consentimento não encontrado"));

        if (!consent.isActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Consentimento já está revogado");
        }

        consent.setActive(false);
        consent.setRevokedAt(LocalDateTime.now());
        repository.save(consent);
        log.info("Atualizado consentimento para revogado no Postgres: id={}", id);

        RevokedConsent revoked = new RevokedConsent();
        revoked.setId(consent.getId());
        revoked.setUserId(consent.getUserId());
        revoked.setCreatedAt(consent.getCreatedAt());
        revoked.setRevokedAt(consent.getRevokedAt());
        revokedConsentRepository.save(revoked);
        log.info("Consentimento regovado salvo no mongoDb: id={}", id);
    }

    @Override
    public List<ConsentResponse> getActiveConsents()
    {
        List<Consent> consentList = repository.findByActiveTrue();
        log.info("Total de consentimentos ativos revogados: {}", consentList.size());
        return consentList.stream()
                .map(ConsentMapper::mapConsentToResponse)
                .toList();
    }

    @Override
    public List<RevokedConsentResponse> getRevokedConsents()
    {
        List<RevokedConsent> revokedConsentsList = revokedConsentRepository.findAll();
        log.info("Total de consentimentos revogados retornados: {}", revokedConsentsList.size());
        return revokedConsentsList.stream()
                .map(ConsentMapper::mapRevokedConsentToResponse)
                .toList();
    }
}
