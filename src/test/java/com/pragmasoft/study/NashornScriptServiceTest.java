package com.pragmasoft.study;

import com.pragmasoft.study.model.ScriptModel;
import com.pragmasoft.study.model.ScriptStatus;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        ScriptModel scriptModel = nashornScriptService.addScript(code);
        assertThat(scriptModel.getId(), is(not(blankString())));
        assertEquals(code, scriptModel.getScriptCode());
        assertEquals(ScriptStatus.CREATED, scriptModel.getScriptStatus());
        assertNotNull(scriptModel.getScriptThread());
        Mockito.verify(nashornScriptRepository, Mockito.times(1)).save(scriptModel);
    }

    @Test
    void addScriptWithCorrectScriptShouldReturnResult() throws InterruptedException {
        ScriptModel scriptModel = nashornScriptService.addScript("print('test')");
        Thread.sleep(2000);
        assertEquals(ScriptStatus.COMPLETED, scriptModel.getScriptStatus());
        assertEquals("test\r\n", scriptModel.getResult());
    }

    @Test
    void addScriptWithIncorrectScriptShouldSetStatusFailed() throws InterruptedException {
        ScriptModel scriptModel = nashornScriptService.addScript("some unsupported code");
        Thread.sleep(2000);
        assertEquals(ScriptStatus.FAILED, scriptModel.getScriptStatus());
        assertThat(scriptModel.getResult(), is(blankString()));
    }

    @Test
    void getAllScripts() {
        assertNotNull(nashornScriptService.getAllScripts());
        Mockito.verify(nashornScriptRepository, Mockito.times(1)).findAll();
    }

    @Test
    void getScriptById() {
        String id = UUID.randomUUID().toString();
        assertFalse(nashornScriptService.getScriptById(id).isPresent());
        Mockito.verify(nashornScriptRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void deleteById() {
        String id = UUID.randomUUID().toString();
        ScriptModel mockScriptModel = Mockito.mock(ScriptModel.class);
        Mockito.when(nashornScriptRepository.findById(id)).thenReturn(Optional.ofNullable(mockScriptModel));
        assertTrue(nashornScriptService.deleteById(id));
        Mockito.verify(mockScriptModel, Mockito.times(1)).stopScriptExecution();
        Mockito.verify(nashornScriptRepository, Mockito.times(1)).findById(id);
        Mockito.verify(nashornScriptRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void getScriptCodeById() {
        String scriptCode = "print('test')";
        String id = UUID.randomUUID().toString();
        ScriptModel mockScriptModel = Mockito.mock(ScriptModel.class);
        Mockito.when(mockScriptModel.getScriptCode()).thenReturn(scriptCode);
        Mockito.when(nashornScriptRepository.findById(id)).thenReturn(Optional.of(mockScriptModel));
        Optional<String> optionalScriptCode = nashornScriptService.getScriptCodeById(id);
        assertTrue(optionalScriptCode.isPresent());
        assertEquals(scriptCode, optionalScriptCode.get());
        Mockito.verify(nashornScriptRepository, Mockito.times(1)).findById(id);
        Mockito.verify(mockScriptModel, Mockito.times(1)).getScriptCode();
    }

    @Test
    void getScriptStatusById() {
        ScriptStatus scriptStatus = ScriptStatus.CREATED;
        String id = UUID.randomUUID().toString();
        ScriptModel mockScriptModel = Mockito.mock(ScriptModel.class);
        Mockito.when(mockScriptModel.getScriptStatus()).thenReturn(scriptStatus);
        Mockito.when(nashornScriptRepository.findById(id)).thenReturn(Optional.of(mockScriptModel));
        Optional<String> optionalScriptStatus = nashornScriptService.getScriptStatusById(id);
        assertTrue(optionalScriptStatus.isPresent());
        assertEquals(scriptStatus.toString(), optionalScriptStatus.get());
        Mockito.verify(nashornScriptRepository, Mockito.times(1)).findById(id);
        Mockito.verify(mockScriptModel, Mockito.times(1)).getScriptStatus();
    }

    @Test
    void getScriptResultById() {
        String scriptResult = "Some result";
        String id = UUID.randomUUID().toString();
        ScriptModel mockScriptModel = Mockito.mock(ScriptModel.class);
        Mockito.when(mockScriptModel.getResult()).thenReturn(scriptResult);
        Mockito.when(nashornScriptRepository.findById(id)).thenReturn(Optional.of(mockScriptModel));
        Optional<String> optionalScriptResult = nashornScriptService.getScriptResultById(id);
        assertTrue(optionalScriptResult.isPresent());
        assertEquals(scriptResult, optionalScriptResult.get());
        Mockito.verify(nashornScriptRepository, Mockito.times(1)).findById(id);
        Mockito.verify(mockScriptModel, Mockito.times(1)).getResult();
    }

    @Test
    void stopScriptExecutionById() {
        String id = UUID.randomUUID().toString();
        ScriptModel mockScriptModel = Mockito.mock(ScriptModel.class);
        Mockito.when(nashornScriptRepository.findById(id)).thenReturn(Optional.of(mockScriptModel));
        assertTrue(nashornScriptService.stopScriptExecutionById(id));
        Mockito.verify(mockScriptModel, Mockito.times(1)).stopScriptExecution();
        Mockito.verify(mockScriptModel, Mockito.times(1)).setScriptStatus(ScriptStatus.STOPPED);

    }
}
