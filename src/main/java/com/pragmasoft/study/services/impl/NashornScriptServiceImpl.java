package com.pragmasoft.study.services.impl;

import com.pragmasoft.study.model.ScriptModel;
import com.pragmasoft.study.model.ScriptStatus;
import com.pragmasoft.study.repository.NashornScriptRepository;
import com.pragmasoft.study.services.NashornScriptService;
import com.pragmasoft.study.threads.NashornScriptThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
public class NashornScriptServiceImpl implements NashornScriptService {

    private static final Logger LOG = LoggerFactory.getLogger(NashornScriptServiceImpl.class);

    private NashornScriptRepository nashornScriptRepository;

    @Autowired
    public NashornScriptServiceImpl(NashornScriptRepository nashornScriptRepository) {
        this.nashornScriptRepository = nashornScriptRepository;
    }

    @Override
    public ScriptModel addScript(String scriptCode) {
        String generatedId = UUID.randomUUID().toString();
        ScriptModel scriptModel = new ScriptModel();
        scriptModel.setId(generatedId);
        scriptModel.setScriptCode(scriptCode);
        NashornScriptThread scriptThread = new NashornScriptThread(scriptModel);
        scriptThread.start();
        scriptModel.setScriptThread(scriptThread);
        nashornScriptRepository.save(scriptModel);
        LOG.debug("Created and started new script: {}", scriptModel);
        return scriptModel;
    }

    @Override
    public Collection<ScriptModel> getAllScripts() {
        return nashornScriptRepository.findAll();
    }

    @Override
    public Optional<ScriptModel> getScriptById(String id) {
        return nashornScriptRepository.findById(id);
    }

    @Override
    public boolean deleteById(String id) {
        Optional<ScriptModel> scriptModel = nashornScriptRepository.findById(id);
        if (scriptModel.isPresent()) {
            scriptModel.get().stopScriptExecution();
            nashornScriptRepository.deleteById(id);
            LOG.debug("Script with id '{}' was removed", id);
            return true;
        } else {
            LOG.debug("Script with id '{}' does not exist", id);
            return false;
        }
    }

    @Override
    public Optional<String> getScriptCodeById(String id) {
        Optional<ScriptModel> scriptModel = nashornScriptRepository.findById(id);
        if (scriptModel.isPresent()) {
            return Optional.ofNullable(scriptModel.get().getScriptCode());
        } else {
            LOG.debug("Script with id '{}' does not exist", id);
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> getScriptStatusById(String id) {
        Optional<ScriptModel> scriptModel = nashornScriptRepository.findById(id);
        if (scriptModel.isPresent()) {
            return Optional.ofNullable(scriptModel.get().getScriptStatus().toString());
        } else {
            LOG.debug("Script with id '{}' does not exist", id);
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> getScriptResultById(String id) {
        Optional<ScriptModel> scriptModel = nashornScriptRepository.findById(id);
        if (scriptModel.isPresent()) {
            return Optional.ofNullable(scriptModel.get().getResult());
        } else {
            LOG.debug("Script with id '{}' does not exist", id);
            return Optional.empty();
        }
    }

    @Override
    public boolean stopScriptExecutionById(String id) {
        Optional<ScriptModel> optionalScriptModel = nashornScriptRepository.findById(id);
        if (optionalScriptModel.isPresent()) {
            ScriptModel scriptModel = optionalScriptModel.get();
            scriptModel.stopScriptExecution();
            scriptModel.setScriptStatus(ScriptStatus.STOPPED);
            return true;
        } else {
            LOG.debug("Script with id '{}' does not exist", id);
            return false;
        }
    }
}
