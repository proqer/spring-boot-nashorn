package com.pragmasoft.study.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.pragmasoft.study.threads.NashornScriptThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Objects;

public class ScriptModel {

    @JsonIgnore
    private static final Logger LOG = LoggerFactory.getLogger(ScriptModel.class);

    private String id;

    @JsonIgnore
    private StringWriter stringWriter;

    @JsonIgnore
    private String scriptCode;

    @JsonIgnore
    private NashornScriptThread scriptThread;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String scriptFailedExplanation;

    private LocalDateTime created = LocalDateTime.now();

    private LocalDateTime statusModified = LocalDateTime.now();

    private ScriptStatus scriptStatus = ScriptStatus.CREATED;

    public String getScriptFailedExplanation() {
        return scriptFailedExplanation;
    }

    public void setScriptFailedExplanation(String scriptFailedExplanation) {
        this.scriptFailedExplanation = scriptFailedExplanation;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getStatusModified() {
        return statusModified;
    }

    public ScriptStatus getScriptStatus() {
        return scriptStatus;
    }

    public void setScriptStatus(ScriptStatus scriptStatus) {
        statusModified = LocalDateTime.now();
        this.scriptStatus = scriptStatus;
    }

    public StringWriter getStringWriter() {
        return stringWriter;
    }

    public void setStringWriter(StringWriter stringWriter) {
        this.stringWriter = stringWriter;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String getResult() {
        return stringWriter == null ? "" : stringWriter.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScriptCode() {
        return scriptCode;
    }

    public void setScriptCode(String scriptCode) {
        this.scriptCode = scriptCode;
    }

    public NashornScriptThread getScriptThread() {
        return scriptThread;
    }

    public void setScriptThread(NashornScriptThread scriptThread) {
        this.scriptThread = scriptThread;
    }

    @Override
    public String toString() {
        return "ScriptModel{" +
                "id='" + id + '\'' +
                ", currentResult='" + getResult() + '\'' +
                ", scriptCode='" + scriptCode + '\'' +
                ", created=" + created +
                ", statusModified=" + statusModified +
                ", scriptStatus=" + scriptStatus +
                '}';
    }

    public void stopScriptExecution() {
        if (Objects.isNull(scriptThread)) {
            LOG.debug("Stopping thread {}", Thread.currentThread().getName());
            return;
        }
        LOG.debug("Interrupting thread {}", scriptThread.getName());
        scriptThread.interrupt();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        LOG.debug("Stopping thread {}", scriptThread.getName());
        scriptThread.stop();
    }
}
