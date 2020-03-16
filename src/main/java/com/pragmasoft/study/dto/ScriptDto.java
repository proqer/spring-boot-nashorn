package com.pragmasoft.study.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ScriptDto {

    private String id;

    private String result;

    private LocalDateTime created;

    private LocalDateTime statusModified;

    private ScriptStatus scriptStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getStatusModified() {
        return statusModified;
    }

    public void setStatusModified(LocalDateTime statusModified) {
        this.statusModified = statusModified;
    }

    public ScriptStatus getScriptStatus() {
        return scriptStatus;
    }

    public void setScriptStatus(ScriptStatus scriptStatus) {
        this.scriptStatus = scriptStatus;
    }

    @Override
    public String toString() {
        return "ScriptDto{" +
                "id='" + id + '\'' +
                ", result='" + result + '\'' +
                ", created=" + created +
                ", statusModified=" + statusModified +
                ", scriptStatus=" + scriptStatus +
                '}';
    }
}
