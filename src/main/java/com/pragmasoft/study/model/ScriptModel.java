package com.pragmasoft.study.model;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ScriptModel {

    private String id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //TODO

}
