package com.pragmasoft.study.threads;

import com.pragmasoft.study.model.ScriptModel;
import com.pragmasoft.study.model.ScriptStatus;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.StringWriter;

public class NashornScriptThread extends Thread {

    private static final String NASHORN_ENGINE_NAME = "nashorn";

    private ScriptModel scriptModel;

    public NashornScriptThread(ScriptModel scriptModel) {
        this.scriptModel = scriptModel;
    }

    @Override
    public void run() {
        try (StringWriter stringWriter = new StringWriter()) {
            ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName(NASHORN_ENGINE_NAME);
            scriptEngine.getContext().setWriter(stringWriter);
            scriptModel.setStringWriter(stringWriter);
            scriptModel.setScriptStatus(ScriptStatus.RUNNING);
            scriptEngine.eval(scriptModel.getScriptCode());
            scriptModel.setScriptStatus(ScriptStatus.COMPLETED);
        } catch (ScriptException e) {
            scriptModel.setScriptFailedExplanation(e.getMessage());
            scriptModel.setScriptStatus(ScriptStatus.FAILED);
        } catch (IOException e) {
            scriptModel.setScriptStatus(ScriptStatus.FAILED);
        }
    }

}
