package com.wayne.restservices.controllers;

import com.wayne.restservices.dtos.BuildInfoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/info")
@Tag(name = "Info", description = "Version information for the REST services")
public class InfoController {

    private final BuildProperties buildProperties;

    public InfoController(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Operation(summary = "Get version info")
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "200",
                    description = "build and version information"
            )
    })
    @GetMapping
    public BuildInfoResponseDto getBuildInfo() {

        return new BuildInfoResponseDto(
                buildProperties.getName(),
                buildProperties.getVersion(),
                buildProperties.getTime()
        );
    }
}

