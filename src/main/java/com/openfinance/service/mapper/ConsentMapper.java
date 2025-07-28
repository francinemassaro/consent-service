package com.openfinance.service.mapper;

import com.openfinance.dto.response.ConsentResponse;
import com.openfinance.dto.response.RevokedConsentResponse;
import com.openfinance.model.Consent;
import com.openfinance.model.RevokedConsent;

public class ConsentMapper {

    public static ConsentResponse mapConsentToResponse(Consent consent) {
        ConsentResponse response = new ConsentResponse();
        response.setId(consent.getId());
        response.setUserId(consent.getUserId());
        response.setActive(consent.isActive());
        response.setCreatedAt(consent.getCreatedAt());
        response.setRevokedAt(consent.getRevokedAt());
        return response;
    }

    public static RevokedConsentResponse mapRevokedConsentToResponse(RevokedConsent revoked) {
        RevokedConsentResponse response = new RevokedConsentResponse();
        response.setId(revoked.getId());
        response.setUserId(revoked.getUserId());
        response.setCreatedAt(revoked.getCreatedAt());
        response.setRevokedAt(revoked.getRevokedAt());
        return response;
    }
}
