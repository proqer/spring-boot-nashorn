package com.pragmasoft.study.repository.impl;

import com.pragmasoft.study.domain.Script;
import com.pragmasoft.study.repository.NashornScriptRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class NashornScriptRepositoryImpl implements NashornScriptRepository {

    private Map<String, Script> scripts = new ConcurrentHashMap<>();

    @Override
    public Optional<Script> findById(String id) {
        return Optional.ofNullable(scripts.get(id));
    }

    @Override
    public Collection<Script> findAll() {
        return scripts.values();
    }

    @Override
    public boolean existsById(String id) {
        return scripts.containsKey(id);
    }

    @Override
    public void deleteById(String id) {
        scripts.remove(id);
    }

    @Override
    public void save(Script script) {
        scripts.put(script.getId(), script);
    }
}
