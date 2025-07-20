package com.openfinance.controller;

import com.openfinance.dto.request.CreateConsentRequest;
import com.openfinance.dto.response.ConsentResponse;
import com.openfinance.dto.response.RevokedConsentResponse;
import com.openfinance.service.ConsentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private static final Logger log = LoggerFactory.getLogger(ConsentController.class);
    private final ConsentService consentService;

    public ConsentController(ConsentService consentService)
    {
        this.consentService = consentService;
    }

    @GetMapping
    public ResponseEntity<List<ConsentResponse>> getAllConsents()
    {
        log.info("Buscando lista com todos os consentimentos.");
        List<ConsentResponse> consentList = consentService.getAllConsents();
        return ResponseEntity.ok(consentList);
    }

    @PostMapping("/{institutionId}")
    public ResponseEntity<ConsentResponse> createConsent(
            @PathVariable String institutionId,
            @RequestBody CreateConsentRequest request)
    {
        log.info("Recebida requisição para criar consentimento para userId={} da instituição={}", request.getUserId(), institutionId);
        ConsentResponse response = consentService.createConsent(institutionId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PatchMapping("/{id}/revoke")
    public ResponseEntity<Void> revokeConsent(@PathVariable String id)
    {
        log.info("Recebida requisição para revogar consentimento com id final={}", id);
        consentService.revokeConsent(id);
        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<ConsentResponse>> getActiveConsents()
    {
        log.info("Listando consentimentos ativos");
        List<ConsentResponse> consentList = consentService.getActiveConsents();
        return ResponseEntity.ok(consentList);
    }

    @GetMapping("/revoked")
    public ResponseEntity<List<RevokedConsentResponse>> getRevokedConsents()
    {
        log.info("Listando consentimentos revogados");
        List<RevokedConsentResponse> list = consentService.getRevokedConsents();
        return ResponseEntity.ok(list);
    }
}

