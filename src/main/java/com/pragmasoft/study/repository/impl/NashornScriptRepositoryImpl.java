package com.pragmasoft.study.repository.impl;

import com.pragmasoft.study.model.ScriptModel;
import com.pragmasoft.study.repository.NashornScriptRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class NashornScriptRepositoryImpl implements NashornScriptRepository {

    private Map<String, ScriptModel> scriptModels = new ConcurrentHashMap<>();

    @Override
    public Optional<ScriptModel> findById(String id) {
        return Optional.ofNullable(scriptModels.get(id));
    }

    @Override
    public Collection<ScriptModel> findAll() {
        return scriptModels.values();
    }

    @Override
    public boolean existsById(String id) {
        return scriptModels.containsKey(id);
    }

    @Override
    public void deleteById(String id) {
        scriptModels.remove(id);
    }

    @Override
    public void save(ScriptModel scriptModel) {
        scriptModels.put(scriptModel.getId(), scriptModel);
    }
}
