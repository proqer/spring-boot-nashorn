package com.pragmasoft.study.repository;

import com.pragmasoft.study.model.ScriptModel;

import java.util.Collection;
import java.util.Optional;

public interface NashornScriptRepository {

    Optional<ScriptModel> findById(String id);

    Collection<ScriptModel> findAll();

    boolean existsById(String id);

    void deleteById(String id);

    void save(ScriptModel scriptModel);

}
