package com.pragmasoft.study.services.impl;

import com.pragmasoft.study.services.JavaScriptService;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.UUID;

@Service
public class JavaScriptServiceImpl implements JavaScriptService {

    @Override
    public String addScript(String script) {
        String id = UUID.randomUUID().toString();
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

        //TODO
        return id;
    }

    @Override
    public String[] getAllScripts() {
        //TODO
        return new String[]{"some"};
    }

    @Override
    public String getScriptById(String id) {
        //TODO
        return id;
    }

    @Override
    public String deleteById(String id) {
        //TODO
        return id;
    }
}
