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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ScriptTest {

    private static String validCode;

    private static String invalidCode;

    @Autowired
    private Script script;

    @BeforeAll
    private static void init() throws IOException {
        invalidCode = FileUtils.readFileToString(
                new ClassPathResource("invalid-test-script.js").getFile(), "UTF-8");
        validCode = FileUtils.readFileToString(
                new ClassPathResource("valid-test-script.js").getFile(), "UTF-8");
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
    void asyncEvaluateScript() {
        script.compileScript(validCode);
        //TODO test script.asyncEvaluateScript();
    }

    @Test
    void stop() {
        //TODO test script.stop();
    }
}
