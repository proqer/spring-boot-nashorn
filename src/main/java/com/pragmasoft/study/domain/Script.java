package com.pragmasoft.study.domain;

import com.pragmasoft.study.dto.ScriptStatus;
import com.pragmasoft.study.exception.ScriptCompilationException;
import com.pragmasoft.study.exception.ScriptStoppingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Future;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class Script {

    private static final Logger LOG = LoggerFactory.getLogger(Script.class);

    @Autowired
    private ScriptEngine scriptEngine;

    private String id = UUID.randomUUID().toString();

    private String scriptCode;

    private ScriptStatus scriptStatus;

    private StringWriter stringWriter;

    private LocalDateTime created = LocalDateTime.now();

    private LocalDateTime statusModified;

    private Thread scriptThread;

    private CompiledScript compiledScript;

    private SimpleScriptContext scriptContext;

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

    public StringWriter getStringWriter() {
        return stringWriter;
    }

    public CompiledScript getCompiledScript() {
        return compiledScript;
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

    public Thread getScriptThread() {
        return scriptThread;
    }

    private void initScriptEngineContext() {
        stringWriter = new StringWriter();
        scriptContext = new SimpleScriptContext();
        scriptContext.setWriter(stringWriter);
        scriptContext.setBindings(scriptEngine.createBindings(), ScriptContext.ENGINE_SCOPE);
    }

    public void compileScript(String scriptCode) {
        this.scriptCode = scriptCode;
        initScriptEngineContext();
        try {
            compiledScript = ((Compilable) scriptEngine).compile(scriptCode);
        } catch (ScriptException e) {
            setScriptStatus(ScriptStatus.FAILED);
            stringWriter.append(e.getMessage());
            throw new ScriptCompilationException(e);
        }
    }

    @Async
    public Future<Script> asyncEvaluateScript() {
        scriptThread = Thread.currentThread();
        LOG.debug("Run script in thread: {}", scriptThread.getName());
        setScriptStatus(ScriptStatus.RUNNING);
        try {
            compiledScript.eval(scriptContext);
            setScriptStatus(ScriptStatus.COMPLETED);
        } catch (ScriptException e) {
            setScriptStatus(ScriptStatus.FAILED);
            stringWriter.append(e.getMessage());
        }
        return new AsyncResult<>(this);
    }

    public void stop() {
        try {
            LOG.debug("Trying to stop script execution");
            if (Objects.isNull(scriptThread)) {
                LOG.debug("Thread is null");
                return;
            }
            if (!scriptThread.isAlive()) {
                LOG.debug("Thread {} is not alive, state is {}", scriptThread.getName(), scriptThread.getState());
                return;
            }
            LOG.debug("Interrupting thread {}", scriptThread.getName());
            scriptThread.interrupt();
            Thread.sleep(2000);
            if (!scriptThread.isAlive()) {
                setScriptStatus(ScriptStatus.STOPPED);
                return;
            }
            LOG.debug("Stopping thread {}", scriptThread.getName());
            scriptThread.stop();
            setScriptStatus(ScriptStatus.STOPPED);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ScriptStoppingException();
        }
    }
}
