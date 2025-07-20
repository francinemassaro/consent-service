package com.openfinance.service;

import com.openfinance.dto.request.CreateConsentRequest;
import com.openfinance.dto.response.ConsentResponse;
import com.openfinance.dto.response.RevokedConsentResponse;

import java.util.List;

public interface ConsentService {
    List<ConsentResponse> getAllConsents();
    ConsentResponse createConsent(String institutionId, CreateConsentRequest request);
    void revokeConsent(String id);
    List<ConsentResponse> getActiveConsents();
    List<RevokedConsentResponse> getRevokedConsents();
}