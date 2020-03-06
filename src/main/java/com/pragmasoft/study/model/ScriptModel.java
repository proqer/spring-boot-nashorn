package com.pragmasoft.study.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.StringWriter;
import java.time.LocalDateTime;

public class ScriptModel {

    private String id;

    @JsonIgnore
    private StringWriter stringWriter;

    @JsonIgnore
    private String scriptCode;

    private LocalDateTime created = LocalDateTime.now();

    private LocalDateTime statusModified = LocalDateTime.now();

    private ScriptStatus scriptStatus = ScriptStatus.CREATED;

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
}
