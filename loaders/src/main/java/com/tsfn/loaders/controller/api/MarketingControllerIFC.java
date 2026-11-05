package com.tsfn.loaders.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;

public interface MarketingControllerIFC {

    @Operation(summary = "Trigger manual : check if has a new file in github")
    @ApiResponse(responseCode = "200", description = "Successfully triggered the manual scan")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @ApiResponse(responseCode = "404", description = "Resource not found")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @GetMapping("/trigger")
    public void triggerManual();


}
