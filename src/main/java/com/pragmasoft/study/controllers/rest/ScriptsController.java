package com.pragmasoft.study.controllers.rest;


import com.pragmasoft.study.model.ScriptModel;
import com.pragmasoft.study.services.JavaScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1")
public class ScriptsController {

    private JavaScriptService javaScriptService;

    @Autowired
    public ScriptsController(JavaScriptService javaScriptService) {
        this.javaScriptService = javaScriptService;
    }

    @PostMapping("/scripts")
    public ResponseEntity<ScriptModel> addScript(@RequestParam("code") String code) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(javaScriptService.addScript(code));
    }

    @GetMapping("/scripts")
    public ResponseEntity<ScriptModel[]> getAllScripts() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(javaScriptService.getAllScripts());
    }

    @GetMapping("/scripts/{id}")
    public ResponseEntity<ScriptModel> getScriptById(@PathVariable("id") String id) {
        ScriptModel scriptModel = javaScriptService.getScriptById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(scriptModel);
    }

    @DeleteMapping("/scripts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable("id") String id) {
        if (!javaScriptService.deleteById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
