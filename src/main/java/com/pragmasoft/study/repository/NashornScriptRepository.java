package com.pragmasoft.study.repository;

import com.pragmasoft.study.domain.Script;

import java.util.Collection;
import java.util.Optional;

public interface NashornScriptRepository {

    Optional<Script> findById(String id);

    Collection<Script> findAll();

    boolean existsById(String id);

    void deleteById(String id);

    void save(Script script);

}
