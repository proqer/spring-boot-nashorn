package com.pragmasoft.study.services;

import com.pragmasoft.study.model.ScriptModel;

import java.util.Collection;
import java.util.Optional;

public interface NashornScriptService {

    /**
     * Asynchronously execute given script
     *
     * @param scriptCode to be executed
     * @return Created script model
     */
    ScriptModel addScript(String scriptCode);

    /**
     * Get information about all scripts
     *
     * @return Collection with all existing scripts
     */
    Collection<ScriptModel> getAllScripts();

    /**
     * Get information about script with given id
     *
     * @return Optional of script model by given id
     */
    Optional<ScriptModel> getScriptById(String id);

    /**
     * @param id of script to be deleted
     * @return true if script was deleted, false otherwise
     */
    boolean deleteById(String id);

    /**
     * @param id of script
     * @return Optional of script code by given id
     */
    Optional<String> getScriptCodeById(String id);

    /**
     * @param id of script
     * @return Optional of script status by given id
     */
    Optional<String> getScriptStatusById(String id);

    /**
     * @param id of script
     * @return Optional of script result by given id
     */
    Optional<String> getScriptResultById(String id);

}
