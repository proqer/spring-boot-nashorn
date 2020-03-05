package com.pragmasoft.study.controllers.rest;

import com.pragmasoft.study.model.ScriptModel;
import com.pragmasoft.study.services.NashornScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/scripts")
public class ScriptsController {

    private NashornScriptService nashornScriptService;

    @Autowired
    public ScriptsController(NashornScriptService nashornScriptService) {
        this.nashornScriptService = nashornScriptService;
    }

    @PostMapping
    public ResponseEntity<ScriptModel> addScript(@RequestParam("code") String code) {
        return ResponseEntity
                .accepted()
                .body(nashornScriptService.addScript(code));
    }

    @GetMapping
    public ResponseEntity<ScriptModel[]> getAllScripts() {
        return ResponseEntity
                .ok()
                .body(nashornScriptService.getAllScripts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScriptModel> getScriptById(@PathVariable("id") String id) {
        ScriptModel scriptModel = nashornScriptService.getScriptById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity
                .ok()
                .body(scriptModel);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") String id) {
        if (!nashornScriptService.deleteById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
