package com.pragmasoft.study.services.impl;

import com.pragmasoft.study.model.ScriptModel;
import com.pragmasoft.study.model.ScriptStatus;
import com.pragmasoft.study.services.NashornScriptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class NashornScriptServiceImpl implements NashornScriptService {

    private static final Logger LOG = LoggerFactory.getLogger(NashornScriptServiceImpl.class);

    private Map<String, ScriptModel> scriptModels = new ConcurrentHashMap<>();

    private ExecutorService executorService = Executors.newSingleThreadExecutor(); //TODO async spring, DI

    @Override
    public ScriptModel addScript(String scriptCode) {
        String generatedId = UUID.randomUUID().toString();
        ScriptModel scriptModel = new ScriptModel();
        scriptModel.setId(generatedId);
        scriptModel.setScriptCode(scriptCode);
//        Future<ScriptModel> futureScript = executorService.submit(new NashornScriptCallable(scriptModel, scriptCode));//TODO
        //TODO Start script execution
//        Thread scriptThread = new Thread();
//        scriptThread.start();
        scriptModels.put(generatedId, scriptModel);
        LOG.debug("Created and started new script: {}", scriptModel);
        return scriptModel;
    }

    @Override
    public Collection<ScriptModel> getAllScripts() {
        return scriptModels.values();
    }

    @Override
    public Optional<ScriptModel> getScriptById(String id) {
        return Optional.ofNullable(scriptModels.get(id));
    }

    @Override
    public boolean deleteById(String id) {
//        if (futureResults.containsKey(id)) {
//            futureResults.get(id).cancel(true);
//            futureResults.remove(id);
//        }
        if (scriptModels.containsKey(id)) {
            //TODO kill thread
            scriptModels.remove(id);
            LOG.debug("Script with id '{}' was removed", id);
            return true;
        } else {
            LOG.debug("Script with id '{}' does not exist", id);
            return false;
        }
    }

    @Override
    public Optional<String> getScriptCodeById(String id) {
        if (scriptModels.containsKey(id)) {
            return Optional.ofNullable(scriptModels.get(id).getScriptCode());
        } else {
            LOG.debug("Script with id '{}' does not exist", id);
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> getScriptStatusById(String id) {
        if (scriptModels.containsKey(id)) {
            return Optional.ofNullable(scriptModels.get(id).getScriptStatus().toString());
        } else {
            LOG.debug("Script with id '{}' does not exist", id);
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> getScriptResultById(String id) {
        if (scriptModels.containsKey(id)) {
            return Optional.ofNullable(scriptModels.get(id).getResult());
        } else {
            LOG.debug("Script with id '{}' does not exist", id);
            return Optional.empty();
        }
    }

    @Override
    public boolean stopScriptExecutionById(String id) {
        if (scriptModels.containsKey(id)) {
            ScriptModel scriptModel = scriptModels.get(id);
            //TODO stop script execution
            scriptModel.setScriptStatus(ScriptStatus.STOPPED);
            return true;
        } else {
            LOG.debug("Script with id '{}' does not exist", id);
            return false;
        }
    }
}
