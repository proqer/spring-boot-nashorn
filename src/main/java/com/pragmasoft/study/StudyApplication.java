package com.pragmasoft.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

@SpringBootApplication
@EnableAsync
public class StudyApplication {

	private static final String NASHORN_ENGINE_NAME = "nashorn";

	public static void main(String[] args) {
		SpringApplication.run(StudyApplication.class, args);
	}

	@Bean
	public ScriptEngine getScriptEngine() {
		return new ScriptEngineManager().getEngineByName(NASHORN_ENGINE_NAME);
	}

}
