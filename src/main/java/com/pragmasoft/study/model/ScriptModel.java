package com.pragmasoft.study.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.StringWriter;

public class ScriptModel {

    private String id;

    @JsonIgnore
    private StringWriter stringWriter;

    @JsonIgnore
    private String scriptCode;

    //TODO datetime from, to

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
                ", scriptResult='" + getResult() + '\'' +
                ", scriptCode='" + scriptCode + '\'' +
                ", scriptStatus=" + scriptStatus +
                '}';
    }
}
