package com.openfinance.repository;

import com.openfinance.model.Consent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ConsentRepository extends JpaRepository<Consent, String> {
    List<Consent> findByActiveTrue();
}
