package com.salesforce.controller.api;

import com.salesforce.bean.Action;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

public interface ActionsControllerIFC {


    @Operation(summary = "Get all active actions", tags = {"Action", "Get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Action.class))
            }),
            @ApiResponse(responseCode = "201", description = "Resource created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Action.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/active/all")
    public List<Action> getAllActionsActive();

    @Operation(summary = "Get all active actions", tags = {"Action", "Get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Action.class))
            }),
            @ApiResponse(responseCode = "404", description = "No actions found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/history/all")
    public List<Action> getAllActionsHistory();

    @Operation(summary = "Get active actions by account ID", tags = {"Action", "Get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Action.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "No actions found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/active/{accountId}")
    public List<Action> getActionsActiveByAccountId(@PathVariable long accountId);

    @Operation(summary = "Get actions history by account ID", tags = {"Action", "Get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Action.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "No actions found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/history/{accountId}")
    public List<Action> getActionsHistoryByAccountId(@PathVariable long accountId);

    @Operation(summary = "Create an action", tags = {"Action", "Create"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Action created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/create")
    public ResponseEntity<String> createAction(@RequestBody Action action);


    @Operation(summary = "Update an action by ID", tags = {"Action", "Update"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Action updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Action not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<String> updateAction(@PathVariable long id, @RequestBody Action action);

    @Operation(summary = "Delete an action by ID", tags = {"Action", "Delete"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Action deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Action not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAction(@PathVariable long id);

    @Operation(summary = "Restore an action by ID", tags = {"Action", "Restore"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Action restored successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Action not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping("/restore/{id}")
    public ResponseEntity<String> reviveAction(@PathVariable long id);

    @Operation(summary = "Get an action by ID", tags = {"Action", "Get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Action retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Action not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getActionById(@PathVariable long id);

    @Operation(summary = "Enable an action", description = "Endpoint to enable an action by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Action enabled successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping("/enable/{id}")
    public ResponseEntity<String> enableAction(@PathVariable long id);

    @Operation(summary = "Disable an action", description = "Endpoint to disable an action by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Action disable successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping("/disable/{id}")
    public ResponseEntity<String> disableAction(@PathVariable long id);
}
