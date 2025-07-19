package com.openfinance.repository;

import com.openfinance.model.Consent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsentRepository extends JpaRepository<Consent, String> {
    List<Consent> findByActiveTrue();
}
