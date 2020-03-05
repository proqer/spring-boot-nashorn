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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/api/v1/scripts")
public class ScriptsController {

    //TODO metrics (Prometheus)

    //TODO Integrate Swagger

    private NashornScriptService nashornScriptService;

    @Autowired
    public ScriptsController(NashornScriptService nashornScriptService) {
        this.nashornScriptService = nashornScriptService;
    }

    /**
     * Asynchronously execute given script
     *
     * @param code to be executed
     * @return response entity with created script model
     */
    @PostMapping
    public ResponseEntity<ScriptModel> addScript(@RequestParam String code) {
        ScriptModel createdScriptModel = nashornScriptService.addScript(code);
        return ResponseEntity
                .accepted()
                .location(URI.create("/api/v1/scripts/" + createdScriptModel.getId()))
                .body(createdScriptModel);
    }

    /**
     * Get information about all scripts
     *
     * @return response entity with all existing scripts
     */
    @GetMapping
    public ResponseEntity<Collection<ScriptModel>> getAllScripts() {
        return ResponseEntity
                .ok()
                .body(nashornScriptService.getAllScripts());
    }

    /**
     * Get information about script with given id
     * If script with given id doesn't exist returns 404 NON_FOUND
     *
     * @param id of script
     * @return script model by given id
     */
    @GetMapping("/{id}")
    public ResponseEntity<ScriptModel> getScriptById(@PathVariable("id") String id) {
        ScriptModel scriptModel = nashornScriptService.getScriptById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity
                .ok()
                .body(scriptModel);
    }

    /**
     * Stop and delete script by given id, returns 204 NO_CONTENT
     * If script with given id doesn't exist returns 404 NON_FOUND
     *
     * @param id of script to be deleted
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") String id) {
        if (!nashornScriptService.deleteById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get status of script with given id
     * If script with given id doesn't exist returns 404 NON_FOUND
     *
     * @param id of script
     * @return plane text status
     */
    @GetMapping("/{id}/status")
    public String getScriptStatus(@PathVariable("id") String id) {
        return nashornScriptService.getScriptStatusById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    /**
     * Update status of script with given id. Stop execution of script
     * If script with given id doesn't exist returns 404 NON_FOUND
     *
     * @param id of script
     * @return plane text status
     */
    @PutMapping("/{id}/status")
    public String updateStatus(@PathVariable("id") String id) {
        if (nashornScriptService.stopScriptExecutionById(id)) {
            //TODO
        }
        return null;
    }

    /**
     * Get code of script with given id
     * If script with given id doesn't exist returns 404 NON_FOUND
     *
     * @param id of script
     * @return plane text code
     */
    @GetMapping("/{id}/code")
    public String getScriptCode(@PathVariable("id") String id) {
        return nashornScriptService.getScriptCodeById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    /**
     * Get result of script with given id
     * If script with given id doesn't exist returns 404 NON_FOUND
     *
     * @param id of script
     * @return plane text result
     */
    @GetMapping("/{id}/result")
    public String getScriptResult(@PathVariable("id") String id) {
        return nashornScriptService.getScriptResultById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

}
