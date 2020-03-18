package com.pragmasoft.study;

import com.pragmasoft.study.domain.Script;
import com.pragmasoft.study.dto.ScriptStatus;
import com.pragmasoft.study.exception.ScriptCompilationException;
import com.pragmasoft.study.exception.ScriptNotFoundException;
import com.pragmasoft.study.repository.NashornScriptRepository;
import com.pragmasoft.study.services.NashornScriptService;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.blankString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class NashornScriptServiceTest {

    @Autowired
    private NashornScriptService nashornScriptService;

    @MockBean
    private NashornScriptRepository nashornScriptRepository;

    private static String validCode;

    private static String invalidCode;

    @BeforeAll
    private static void init() throws IOException {
        invalidCode = FileUtils.readFileToString(
                new ClassPathResource("invalid-test-script.js").getFile(), "UTF-8");
        validCode = FileUtils.readFileToString(
                new ClassPathResource("valid-test-script.js").getFile(), "UTF-8");
    }

    @Test
    void addValidScript() {
        String id = nashornScriptService.addScript(validCode);
        assertThat(id, is(not(blankString())));
        verify(nashornScriptRepository, times(1)).save(any());
    }

    @Test
    void addInvalidScript() {
        assertThrows(ScriptCompilationException.class, () -> nashornScriptService.addScript(invalidCode));
        verify(nashornScriptRepository, never()).save(any());
    }

    @Test
    void getAllScripts() {
        assertNotNull(nashornScriptService.getAllScripts());
        verify(nashornScriptRepository, times(1)).findAll();
    }

    @Test
    void getScriptById() {
        String id = UUID.randomUUID().toString();
        assertThrows(ScriptNotFoundException.class, () -> nashornScriptService.getScriptById(id));
        verify(nashornScriptRepository, times(1)).findById(id);
    }

    @Test
    void deleteById() {
        String id = UUID.randomUUID().toString();
        Script mockScript = mock(Script.class);
        when(nashornScriptRepository.findById(id)).thenReturn(Optional.ofNullable(mockScript));
        assertTrue(nashornScriptService.deleteById(id));
        verify(mockScript, times(1)).stop();
        verify(nashornScriptRepository, times(1)).findById(id);
        verify(nashornScriptRepository, times(1)).deleteById(id);
    }

    @Test
    void getScriptCodeById() {
        String scriptCode = "print('test')";
        String id = UUID.randomUUID().toString();
        Script mockScript = mock(Script.class);
        when(mockScript.getScriptCode()).thenReturn(scriptCode);
        when(nashornScriptRepository.findById(id)).thenReturn(Optional.of(mockScript));
        String scriptCodeById = nashornScriptService.getScriptCodeById(id);
        assertEquals(scriptCode, scriptCodeById);
        verify(nashornScriptRepository, times(1)).findById(id);
        verify(mockScript, times(1)).getScriptCode();
    }

    @Test
    void getScriptStatusById() {
        ScriptStatus scriptStatus = ScriptStatus.CREATED;
        String id = UUID.randomUUID().toString();
        Script mockScript = mock(Script.class);
        when(mockScript.getScriptStatus()).thenReturn(scriptStatus);
        when(nashornScriptRepository.findById(id)).thenReturn(Optional.of(mockScript));
        String scriptStatusById = nashornScriptService.getScriptStatusById(id);
        assertEquals(scriptStatus.toString(), scriptStatusById);
        verify(nashornScriptRepository, times(1)).findById(id);
        verify(mockScript, times(1)).getScriptStatus();
    }

    @Test
    void getScriptResultById() {
        String scriptResult = "Some result";
        String id = UUID.randomUUID().toString();
        Script mockScript = mock(Script.class);
        when(mockScript.getResult()).thenReturn(scriptResult);
        when(nashornScriptRepository.findById(id)).thenReturn(Optional.of(mockScript));
        String scriptResultById = nashornScriptService.getScriptResultById(id);
        assertEquals(scriptResult, scriptResultById);
        verify(nashornScriptRepository, times(1)).findById(id);
        verify(mockScript, times(1)).getResult();
    }

    @Test
    void stopScriptExecutionById() {
        String id = UUID.randomUUID().toString();
        Script mockScript = mock(Script.class);
        when(nashornScriptRepository.findById(id)).thenReturn(Optional.of(mockScript));
        assertTrue(nashornScriptService.stopScriptExecutionById(id));
        verify(mockScript, times(1)).stop();
    }
}
