package com.pragmasoft.study.exception;

import javax.script.ScriptException;

public class ScriptRuntimeException extends RuntimeException {

    private final String message;

    public ScriptRuntimeException(ScriptException e) {
        this.message = e.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
