package com.pragmasoft.study.controllers.rest;


import com.pragmasoft.study.services.JavaScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ScriptsController {

    private JavaScriptService javaScriptService;

    @Autowired
    public ScriptsController(JavaScriptService javaScriptService) {
        this.javaScriptService = javaScriptService;
    }

    @PostMapping("/scripts")
    public String addScript(@RequestParam("code") String code) {
        javaScriptService.addScript(code);
        //TODO return 201
        return javaScriptService.addScript(code);
    }

    @GetMapping("/scripts")
    public String[] getAllScripts(@RequestParam("code") String code) {

        //TODO
        return javaScriptService.getAllScripts();
    }

    @GetMapping("/scripts/{id}")
    public String getScriptById(@PathVariable("id") String id) {

        //TODO
        return javaScriptService.getScriptById(id);
    }

    @DeleteMapping("/scripts/{id}")
    public String deleteById(@PathVariable("id") String id) {

        //TODO
        return id;
    }

}
