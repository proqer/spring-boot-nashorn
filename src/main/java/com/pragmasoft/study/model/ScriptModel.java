package com.pragmasoft.study.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.StringWriter;

public class ScriptModel {

    private String id;

    @JsonIgnore
    private StringWriter stringWriter;

    private ScriptStatus scriptStatus = ScriptStatus.CREATED;

    public ScriptStatus getScriptStatus() {
        return scriptStatus;
    }

    public void setScriptStatus(ScriptStatus scriptStatus) {
        this.scriptStatus = scriptStatus;
    }

    public StringWriter getStringWriter() {
        return stringWriter;
    }

    public void setStringWriter(StringWriter stringWriter) {
        this.stringWriter = stringWriter;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getResult() {
        return stringWriter == null ? null : stringWriter.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
