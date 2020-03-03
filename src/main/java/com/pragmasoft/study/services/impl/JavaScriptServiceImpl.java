package com.pragmasoft.study.services.impl;

import com.pragmasoft.study.model.ScriptModel;
import com.pragmasoft.study.services.JavaScriptService;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class JavaScriptServiceImpl implements JavaScriptService {

    private List<ScriptModel> scriptModels = new ArrayList<>();

    @Override
    public ScriptModel addScript(String script) {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        //TODO
        ScriptModel scriptModel = new ScriptModel();
        scriptModel.setId(UUID.randomUUID().toString());
        scriptModels.add(scriptModel);
        return scriptModel;
    }

    @Override
    public ScriptModel[] getAllScripts() {
        //TODO
        return scriptModels.toArray(new ScriptModel[0]);
    }

    @Override
    public Optional<ScriptModel> getScriptById(String id) {
        //TODO
        return scriptModels.stream()
                .filter((scriptModel) -> scriptModel.getId().equals(id))
                .findFirst();
    }

    @Override
    public boolean deleteById(String id) {
        //TODO
        return scriptModels.removeIf((scriptModel) -> scriptModel.getId().equals(id));
    }
}
