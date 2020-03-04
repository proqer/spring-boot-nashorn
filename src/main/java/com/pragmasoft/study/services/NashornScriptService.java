package com.pragmasoft.study.services;

import com.pragmasoft.study.model.ScriptModel;

import java.util.Optional;

public interface NashornScriptService {

    ScriptModel addScript(String scriptCode);

    ScriptModel[] getAllScripts();

    Optional<ScriptModel> getScriptById(String id);

    boolean deleteById(String id);

}
