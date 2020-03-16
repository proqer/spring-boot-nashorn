package com.pragmasoft.study.domain;

import com.pragmasoft.study.dto.ScriptStatus;
import com.pragmasoft.study.exception.ScriptCompilationException;
import com.pragmasoft.study.exception.ScriptRuntimeException;
import com.pragmasoft.study.exception.ScriptStoppingException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Script {

    private static final String NASHORN_ENGINE_NAME = "nashorn";

    private static final Logger LOG = LoggerFactory.getLogger(Script.class);

    private String id = UUID.randomUUID().toString();

    private String scriptCode;

    private ScriptStatus scriptStatus;

    private StringWriter stringWriter;

    private LocalDateTime created = LocalDateTime.now();

    private LocalDateTime statusModified;

    private Thread scriptThread;

    public ScriptStatus getScriptStatus() {
        return scriptStatus;
    }

    public void setScriptStatus(ScriptStatus scriptStatus) {
        statusModified = LocalDateTime.now();
        this.scriptStatus = scriptStatus;
    }

    public String getResult() {
        return stringWriter == null ? "" : stringWriter.toString();
    }

    public String getId() {
        return id;
    }

    public String getScriptCode() {
        return scriptCode;
    }

    public void setScriptCode(String scriptCode) {
        this.scriptCode = scriptCode;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getStatusModified() {
        return statusModified;
    }

    @Async
    public void start() {
        scriptThread = Thread.currentThread();
        LOG.debug("Run script in thread: {}", scriptThread.getName());
        ScriptEngine scriptEngine = createScriptEngine();
        CompiledScript compiledScript = compileScript(scriptEngine);
        evaluateScript(compiledScript);
    }

    private ScriptEngine createScriptEngine() {
        ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName(NASHORN_ENGINE_NAME);
        stringWriter = new StringWriter();
        scriptEngine.getContext().setWriter(stringWriter);
        return scriptEngine;
    }

    private CompiledScript compileScript(ScriptEngine scriptEngine) {
        CompiledScript compiledScript;
        try {
            compiledScript = ((Compilable) scriptEngine).compile(scriptCode);
        } catch (ScriptException e) {
            setScriptStatus(ScriptStatus.FAILED);
            throw new ScriptCompilationException(e);
        }
        return compiledScript;
    }

    private void evaluateScript(CompiledScript compiledScript) {
        setScriptStatus(ScriptStatus.RUNNING);
        try {
            compiledScript.eval();
        } catch (ScriptException e) {
            setScriptStatus(ScriptStatus.FAILED);
            throw new ScriptRuntimeException(e);
        }
        setScriptStatus(ScriptStatus.COMPLETED);
    }

    public void stop() {
        try {
            LOG.debug("Trying to stop script execution");
            if (Objects.isNull(scriptThread)) {
                LOG.debug("Thread is null");
                return;
            }
            if (!scriptThread.isAlive()) {
                LOG.debug("Thread {} is not alive", scriptThread.getName());
                return;
            }
            LOG.debug("Interrupting thread {}", scriptThread.getName());
            scriptThread.interrupt();
            Thread.sleep(2000);
            if (!scriptThread.isAlive()) {
                return;
            }
            LOG.debug("Stopping thread {}", scriptThread.getName());
            scriptThread.stop();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ScriptStoppingException();
        } finally {
            setScriptStatus(ScriptStatus.STOPPED);
            IOUtils.closeQuietly(stringWriter);
        }
    }
}

























