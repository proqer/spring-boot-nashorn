package com.pragmasoft.study.services;

import com.pragmasoft.study.domain.Script;

import java.util.Collection;

public interface NashornScriptService {

    /**
     * Asynchronously execute given script
     *
     * @param scriptCode to be executed
     * @return Id of created script
     */
    String addScript(String scriptCode);

    /**
     * Get information about all scripts
     *
     * @return Collection with all existing scripts
     */
    Collection<Script> getAllScripts();

    /**
     * Get information about script with given id
     *
     * @return Script by given id
     */
    Script getScriptById(String id);

    /**
     * Delete script with given id
     *
     * @param id of script to be deleted
     * @return true if script was found and deleted, false otherwise
     */
    boolean deleteById(String id);

    /**
     * Get code of script with given id
     *
     * @param id of script
     * @return Script code by given id
     */
    String getScriptCodeById(String id);

    /**
     * Get current status of script with given id
     *
     * @param id of script
     * @return Script status by given id
     */
    String getScriptStatusById(String id);

    /**
     * Get current result of script with given id
     *
     * @param id of script
     * @return Script result by given id
     */
    String getScriptResultById(String id);


    /**
     * Stop script execution
     *
     * @param id of script to be stopped
     * @return true if script was found and stopped, false otherwise
     */
    boolean stopScriptExecutionById(String id);

}
