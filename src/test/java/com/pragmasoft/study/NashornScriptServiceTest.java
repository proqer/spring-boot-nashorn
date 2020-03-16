package com.pragmasoft.study;

import com.pragmasoft.study.domain.Script;
import com.pragmasoft.study.dto.ScriptStatus;
import com.pragmasoft.study.exception.ScriptNotFoundException;
import com.pragmasoft.study.repository.NashornScriptRepository;
import com.pragmasoft.study.services.NashornScriptService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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

@SpringBootTest
public class NashornScriptServiceTest {

    @Autowired
    private NashornScriptService nashornScriptService;

    @MockBean
    private NashornScriptRepository nashornScriptRepository;

    @Test
    void addScript() {
        String code = "print('test')";
        String id = nashornScriptService.addScript(code);
        assertThat(id, is(not(blankString())));
        Mockito.verify(nashornScriptRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void getAllScripts() {
        assertNotNull(nashornScriptService.getAllScripts());
        Mockito.verify(nashornScriptRepository, Mockito.times(1)).findAll();
    }

    @Test
    void getScriptById() {
        String id = UUID.randomUUID().toString();
        assertThrows(ScriptNotFoundException.class, () -> nashornScriptService.getScriptById(id));
        Mockito.verify(nashornScriptRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void deleteById() {
        String id = UUID.randomUUID().toString();
        Script mockScript = Mockito.mock(Script.class);
        Mockito.when(nashornScriptRepository.findById(id)).thenReturn(Optional.ofNullable(mockScript));
        assertTrue(nashornScriptService.deleteById(id));
        Mockito.verify(mockScript, Mockito.times(1)).stop();
        Mockito.verify(nashornScriptRepository, Mockito.times(1)).findById(id);
        Mockito.verify(nashornScriptRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void getScriptCodeById() {
        String scriptCode = "print('test')";
        String id = UUID.randomUUID().toString();
        Script mockScript = Mockito.mock(Script.class);
        Mockito.when(mockScript.getScriptCode()).thenReturn(scriptCode);
        Mockito.when(nashornScriptRepository.findById(id)).thenReturn(Optional.of(mockScript));
        String scriptCodeById = nashornScriptService.getScriptCodeById(id);
        assertEquals(scriptCode, scriptCodeById);
        Mockito.verify(nashornScriptRepository, Mockito.times(1)).findById(id);
        Mockito.verify(mockScript, Mockito.times(1)).getScriptCode();
    }

    @Test
    void getScriptStatusById() {
        ScriptStatus scriptStatus = ScriptStatus.CREATED;
        String id = UUID.randomUUID().toString();
        Script mockScript = Mockito.mock(Script.class);
        Mockito.when(mockScript.getScriptStatus()).thenReturn(scriptStatus);
        Mockito.when(nashornScriptRepository.findById(id)).thenReturn(Optional.of(mockScript));
        String scriptStatusById = nashornScriptService.getScriptStatusById(id);
        assertEquals(scriptStatus.toString(), scriptStatusById);
        Mockito.verify(nashornScriptRepository, Mockito.times(1)).findById(id);
        Mockito.verify(mockScript, Mockito.times(1)).getScriptStatus();
    }

    @Test
    void getScriptResultById() {
        String scriptResult = "Some result";
        String id = UUID.randomUUID().toString();
        Script mockScript = Mockito.mock(Script.class);
        Mockito.when(mockScript.getResult()).thenReturn(scriptResult);
        Mockito.when(nashornScriptRepository.findById(id)).thenReturn(Optional.of(mockScript));
        String scriptResultById = nashornScriptService.getScriptResultById(id);
        assertEquals(scriptResult, scriptResultById);
        Mockito.verify(nashornScriptRepository, Mockito.times(1)).findById(id);
        Mockito.verify(mockScript, Mockito.times(1)).getResult();
    }

    @Test
    void stopScriptExecutionById() {
        String id = UUID.randomUUID().toString();
        Script mockScript = Mockito.mock(Script.class);
        Mockito.when(nashornScriptRepository.findById(id)).thenReturn(Optional.of(mockScript));
        assertTrue(nashornScriptService.stopScriptExecutionById(id));
        Mockito.verify(mockScript, Mockito.times(1)).stop();
        Mockito.verify(mockScript, Mockito.times(1)).setScriptStatus(ScriptStatus.STOPPED);

    }
}
