package com.pragmasoft.study.services.impl;

import com.pragmasoft.study.model.ScriptModel;
import com.pragmasoft.study.services.NashornScriptService;
import com.pragmasoft.study.threads.NashornScriptCallable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class NashornScriptServiceImpl implements NashornScriptService {

    private List<ScriptModel> scriptModels = new ArrayList<>();

    private Map<String, Future<ScriptModel>> futureResults = new HashMap<>();

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public ScriptModel addScript(String scriptCode) {
        String generatedId = UUID.randomUUID().toString();
        ScriptModel scriptModel = new ScriptModel();
        scriptModel.setId(generatedId);
        Future<ScriptModel> future = executorService.submit(new NashornScriptCallable(scriptModel, scriptCode));
        futureResults.put(generatedId, future);
        scriptModels.add(scriptModel);
        return scriptModel;
    }

    @Override
    public ScriptModel[] getAllScripts() {
        return scriptModels.toArray(new ScriptModel[0]);
    }

    @Override
    public Optional<ScriptModel> getScriptById(String id) {
        return scriptModels.stream()
                .filter((scriptModel) -> scriptModel.getId().equals(id))
                .findFirst();
    }

    @Override
    public boolean deleteById(String id) {
        if (futureResults.containsKey(id)) {
            futureResults.get(id).cancel(true);
            futureResults.remove(id);
        }
        return scriptModels.removeIf((scriptModel) -> scriptModel.getId().equals(id));
    }
}
