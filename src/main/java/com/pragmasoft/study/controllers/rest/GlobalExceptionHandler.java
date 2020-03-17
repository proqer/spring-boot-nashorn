package com.pragmasoft.study.controllers.rest;

import com.pragmasoft.study.exception.ScriptCompilationException;
import com.pragmasoft.study.exception.ScriptNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ScriptNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void onScriptNotFoundException(ScriptNotFoundException e) {
        LOG.debug("Script with id {} not found", e.getId());
    }

    @ExceptionHandler(ScriptCompilationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String onScriptCompilationException(ScriptCompilationException e) {
        LOG.debug("Compilation exception: {}", e.getMessage());
        return "Compilation failed: " + e.getMessage();
    }

}
