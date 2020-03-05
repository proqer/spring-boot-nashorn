package com.pragmasoft.study.threads;

import com.pragmasoft.study.model.ScriptModel;
import com.pragmasoft.study.model.ScriptStatus;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.concurrent.Callable;

public class NashornScriptCallable implements Callable<ScriptModel> {

    private static final String NASHORN_ENGINE_NAME = "nashorn";

    private ScriptModel scriptModel;

    private String scriptCode;

    public NashornScriptCallable(ScriptModel scriptModel, String scriptCode) {
        this.scriptModel = scriptModel;
        this.scriptCode = scriptCode;
    }

    @Override
    public ScriptModel call() {
        try (StringWriter stringWriter = new StringWriter()) {
            ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName(NASHORN_ENGINE_NAME);
            scriptEngine.getContext().setWriter(stringWriter);
            scriptModel.setStringWriter(stringWriter);
            scriptModel.setScriptStatus(ScriptStatus.RUNNING);
            scriptEngine.eval(scriptCode);
            scriptModel.setScriptStatus(ScriptStatus.COMPLETED);
        } catch (ScriptException | IOException e) {
            //TODO Script exception expl.
            scriptModel.setScriptStatus(ScriptStatus.FAILED);
        }
        return scriptModel;
    }

}
