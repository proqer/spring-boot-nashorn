package com.pragmasoft.study.domain;

import com.pragmasoft.study.dto.ScriptStatus;
import com.pragmasoft.study.exception.ScriptCompilationException;
import com.pragmasoft.study.exception.ScriptRuntimeException;
import com.pragmasoft.study.exception.ScriptStoppingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
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

    private CompiledScript compiledScript;

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

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getStatusModified() {
        return statusModified;
    }

    private ScriptEngine createScriptEngine() {
        ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName(NASHORN_ENGINE_NAME);
        stringWriter = new StringWriter();
        scriptEngine.getContext().setWriter(stringWriter);
        return scriptEngine;
    }

    public void compileScript(String scriptCode) {
        this.scriptCode = scriptCode;
        ScriptEngine scriptEngine = createScriptEngine();
        try {
            compiledScript = ((Compilable) scriptEngine).compile(scriptCode);
        } catch (ScriptException e) {
            setScriptStatus(ScriptStatus.FAILED);
            stringWriter.append(e.getMessage());
            throw new ScriptCompilationException(e);
        }
    }

    @Async
    public void asyncEvaluateScript() {
        scriptThread = Thread.currentThread();
        LOG.debug("Run script in thread: {}", scriptThread.getName());
        setScriptStatus(ScriptStatus.RUNNING);
        try {
            compiledScript.eval();
        } catch (ScriptException e) {
            setScriptStatus(ScriptStatus.FAILED);
            stringWriter.append(e.getMessage());
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
            try {
                stringWriter.close();
            } catch (IOException e) {
                //Do nothing
            }
        }
    }
}

























