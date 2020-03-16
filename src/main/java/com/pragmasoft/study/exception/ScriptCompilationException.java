package com.pragmasoft.study.exception;

import javax.script.ScriptException;

public class ScriptCompilationException extends RuntimeException {

    private final String message;

    public ScriptCompilationException(ScriptException e) {
        this.message = e.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
