package com.wayne.restservices.controllers;

import com.wayne.restservices.entities.dto.BuildInfoResponseDto;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/info")
public class InfoController {

    private final BuildProperties buildProperties;

    public InfoController(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @GetMapping
    public BuildInfoResponseDto getBuildInfo() {

        return new BuildInfoResponseDto(
                buildProperties.getName(),
                buildProperties.getVersion(),
                buildProperties.getTime()
        );
    }
}

