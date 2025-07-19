package com.openfinance.repository;

import com.openfinance.model.RevokedConsent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RevokedConsentRepository extends MongoRepository<RevokedConsent, String> {
}
