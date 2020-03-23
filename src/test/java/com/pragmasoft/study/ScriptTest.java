package com.pragmasoft.study;

import com.pragmasoft.study.domain.Script;
import com.pragmasoft.study.dto.ScriptStatus;
import com.pragmasoft.study.exception.ScriptCompilationException;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ScriptTest {

    private static String validCode;

    private static String invalidCode;

    private static String runtimeExceptionCode;

    private static String whileTrueCode;

    @Autowired
    private Script script;

    @Autowired
    private Script secondScript;

    @BeforeAll
    private static void init() throws IOException {
        invalidCode = FileUtils.readFileToString(
                new ClassPathResource("invalid-test-script.js").getFile(), "UTF-8");
        validCode = FileUtils.readFileToString(
                new ClassPathResource("valid-test-script.js").getFile(), "UTF-8");
        runtimeExceptionCode = FileUtils.readFileToString(
                new ClassPathResource("runtime-exception-script.js").getFile(), "UTF-8");
        whileTrueCode = FileUtils.readFileToString(
                new ClassPathResource("while-true-script.js").getFile(), "UTF-8");
    }

    @Test
    void compileValidScript() {
        script.compileScript(validCode);
        assertEquals(validCode, script.getScriptCode());
        assertNotNull(script.getStringWriter());
        assertNotNull(script.getCompiledScript());
    }

    @Test
    void compileInvalidScript() {
        assertThrows(ScriptCompilationException.class, () -> script.compileScript(invalidCode));
        assertEquals(invalidCode, script.getScriptCode());
        assertEquals(ScriptStatus.FAILED, script.getScriptStatus());
        assertNotNull(script.getStringWriter());
    }

    @Test
    void asyncEvaluateValidScript() throws ExecutionException, InterruptedException {
        script.compileScript(validCode);
        Future<Script> scriptFuture = script.asyncEvaluateScript();
        Script scriptResult = scriptFuture.get();
        assertNotNull(scriptResult.getScriptThread());
        assertNotEquals(Thread.State.RUNNABLE, script.getScriptThread().getState());
        assertNotNull(scriptResult.getResult());
        assertEquals(ScriptStatus.COMPLETED, scriptResult.getScriptStatus());
    }

    @Test
    void asyncEvaluateScriptWithRuntimeException() throws ExecutionException, InterruptedException {
        script.compileScript(runtimeExceptionCode);
        Future<Script> scriptFuture = script.asyncEvaluateScript();
        Script scriptResult = scriptFuture.get();
        assertNotNull(scriptResult.getScriptThread());
        assertNotEquals(Thread.State.RUNNABLE, script.getScriptThread().getState());
        assertNotNull(scriptResult.getResult());
        assertEquals(ScriptStatus.FAILED, scriptResult.getScriptStatus());
    }

    @Test
    void scriptIndependence() throws ExecutionException, InterruptedException {
        script.compileScript(validCode);
        secondScript.compileScript(validCode);
        Future<Script> firstScriptFuture = script.asyncEvaluateScript();
        Future<Script> secondScriptFuture = secondScript.asyncEvaluateScript();
        Script firstScriptResult = firstScriptFuture.get();
        Script secondScriptResult = secondScriptFuture.get();
        assertEquals(firstScriptResult.getResult(), secondScriptResult.getResult());
    }

    @Test
    void stop() throws ExecutionException, InterruptedException {
        script.compileScript(whileTrueCode);
        Future<Script> scriptFuture = script.asyncEvaluateScript();
        try {
            scriptFuture.get(2, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            script.stop();
        }
        assertNotNull(script.getScriptThread());
        assertEquals(ScriptStatus.STOPPED, script.getScriptStatus());
    }
}
