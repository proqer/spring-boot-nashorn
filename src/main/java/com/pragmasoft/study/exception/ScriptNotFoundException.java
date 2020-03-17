package com.pragmasoft.study.exception;

public class ScriptNotFoundException extends RuntimeException {

    private final String id;

    public ScriptNotFoundException(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
