package com.openfinance.repository;

import com.openfinance.model.Consent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ConsentRepository {
    private Map<String, Consent> database = new HashMap<>();

    public List<Consent> findAll() {
        return new ArrayList<>(database.values());
    }

    public Optional<Consent> findById(String id) {
        return Optional.ofNullable(database.get(id));
    }

    public void save(Consent consent) {
        database.put(consent.getId(), consent);
    }
}
