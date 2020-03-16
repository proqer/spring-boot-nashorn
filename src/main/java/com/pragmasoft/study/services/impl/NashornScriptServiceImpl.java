package com.pragmasoft.study.services.impl;

import com.pragmasoft.study.domain.Script;
import com.pragmasoft.study.exception.ScriptNotFoundException;
import com.pragmasoft.study.repository.NashornScriptRepository;
import com.pragmasoft.study.services.NashornScriptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class NashornScriptServiceImpl implements NashornScriptService {

    private static final Logger LOG = LoggerFactory.getLogger(NashornScriptServiceImpl.class);

    private NashornScriptRepository nashornScriptRepository;

    @Autowired
    public NashornScriptServiceImpl(NashornScriptRepository nashornScriptRepository) {
        this.nashornScriptRepository = nashornScriptRepository;
    }

    @Override
    public String addScript(String scriptCode) {
        Script script = new Script();
        script.setScriptCode(scriptCode);
        script.start();
        nashornScriptRepository.save(script);
        LOG.debug("Created and started new script: {}", script);
        return script.getId();
    }

    @Override
    public Collection<Script> getAllScripts() {
        return nashornScriptRepository.findAll();
    }

    @Override
    public Script getScriptById(String id) {
        return nashornScriptRepository.findById(id)
                .orElseThrow(ScriptNotFoundException::new);
    }

    @Override
    public boolean deleteById(String id) {
        Optional<Script> script = nashornScriptRepository.findById(id);
        if (script.isPresent()) {
            script.get().stop();
            nashornScriptRepository.deleteById(id);
            LOG.debug("Script with id '{}' was removed", id);
            return true;
        } else {
            LOG.debug("Script with id '{}' does not exist", id);
            return false;
        }
    }

    @Override
    public String getScriptCodeById(String id) {
        return getScriptById(id).getScriptCode();
    }

    @Override
    public String getScriptStatusById(String id) {
        return getScriptById(id).getScriptStatus().toString();
    }

    @Override
    public String getScriptResultById(String id) {
        return getScriptById(id).getResult();
    }

    @Override
    public boolean stopScriptExecutionById(String id) {
        Optional<Script> optionalScript = nashornScriptRepository.findById(id);
        if (optionalScript.isPresent()) {
            Script script = optionalScript.get();
            script.stop();
            return true;
        } else {
            LOG.debug("Script with id '{}' does not exist", id);
            return false;
        }
    }
}
