package com.pragmasoft.study.services;

import com.pragmasoft.study.model.ScriptModel;

import java.util.Optional;

public interface JavaScriptService {

    ScriptModel addScript(String script);

    ScriptModel[] getAllScripts();

    Optional<ScriptModel> getScriptById(String id);

    boolean deleteById(String id);

}
