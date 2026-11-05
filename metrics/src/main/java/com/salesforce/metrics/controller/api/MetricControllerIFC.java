package com.salesforce.metrics.controller.api;

import com.salesforce.metrics.bean.Metric;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface MetricControllerIFC {
    @Operation(summary = "Get all metrics active ")
    @GetMapping("/active/all")
    public List<Metric> getAllActiveMetrics();

    @Operation(summary = "Get all metrics history ")
    @GetMapping("history/all")
    public List<Metric> getAllHistoryMetrics();


    @Operation(summary = "Restore an metric active by account Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Get action successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Action not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/active/{accountId}")
    public List<Metric> getMetricByAccountId(@PathVariable long accountId);

    @Operation(summary = "Restore an metric history by account Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Get action successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Action not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/history/{accountId}")
    public List<Metric> getMetricHistoryByAccountId(@PathVariable long accountId);

    @Operation(summary = "Create a new metric", description = "Endpoint to create a new metric")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Metric created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/create")
    public ResponseEntity<String> createMetric(@RequestBody Metric metric);
}
