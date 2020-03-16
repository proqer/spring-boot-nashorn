package com.pragmasoft.study.dto.mapper;

import com.pragmasoft.study.domain.Script;
import com.pragmasoft.study.dto.ScriptDto;

public class ScriptMapper {

    private ScriptMapper() {
    }

    public static ScriptDto toScriptDto(Script script) {
        ScriptDto scriptDto = new ScriptDto();
        scriptDto.setId(script.getId());
        scriptDto.setCreated(script.getCreated());
        scriptDto.setStatusModified(script.getStatusModified());
        scriptDto.setResult(script.getResult());
        return scriptDto;
    }
}
