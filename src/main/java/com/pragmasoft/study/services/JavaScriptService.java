package com.pragmasoft.study.services;

public interface JavaScriptService {

    String addScript(String script);

    String[] getAllScripts();

    String getScriptById(String id);

    String deleteById(String id);

}
