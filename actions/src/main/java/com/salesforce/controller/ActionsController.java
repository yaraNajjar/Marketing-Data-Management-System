package com.salesforce.controller;

import com.salesforce.bean.Action;
import com.salesforce.controller.api.ActionsControllerIFC;
import com.salesforce.service.ActionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/actions")
@CrossOrigin("*")
public class ActionsController implements ActionsControllerIFC {

    @Autowired
    private ActionsService actionsService;

    @GetMapping("/active/all")
    public List<Action> getAllActionsActive() {
        return actionsService.getAllActionsActive();
    }

    @GetMapping("/history/all")
    public List<Action> getAllActionsHistory() {
        return actionsService.getAllActionsHistory();
    }

    @GetMapping("/active/{accountId}")
    public List<Action> getActionsActiveByAccountId(@PathVariable long accountId) {
        return actionsService.getAllActionsByAccountIdActive(accountId);
    }

    @GetMapping("/history/{accountId}")
    public List<Action> getActionsHistoryByAccountId(@PathVariable long accountId) {
        return actionsService.getAllActionsByAccountIdHistory(accountId);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createAction(@RequestBody Action action) {
        try {
            String actionName = actionsService.createAction(action);
            return ResponseEntity.ok("Action with name " + actionName + " created successfully");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAction(@PathVariable long id, @RequestBody Action action) {
        try {
            String actionName = actionsService.updateAction(id, action);
            return ResponseEntity.ok("Action with name " + actionName + " updated successfully");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAction(@PathVariable long id) {
        try {
            String actionName = actionsService.deleteAction(id);
            return ResponseEntity.ok("Action " + actionName + " deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete action  " + e.getMessage());
        }
    }

    @PutMapping("/disable/{id}")
    public ResponseEntity<String> disableAction(@PathVariable long id) {
        try {
            String actionName = actionsService.disableAction(id);
            return ResponseEntity.ok("Action " + actionName + " disable successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to disable action  " + e.getMessage());
        }
    }

    @PutMapping("/enable/{id}")
    public ResponseEntity<String> enableAction(@PathVariable long id) {
        try {
            String actionName = actionsService.enableAction(id);
            return ResponseEntity.ok("Action " + actionName + " enable successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to enable action  " + e.getMessage());
        }
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<String> reviveAction(@PathVariable long id) {
        try {
            String actionName = actionsService.undeleteAction(id);
            return ResponseEntity.ok("Action " + actionName + " restored successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to restore action " + e.getMessage());
        }
    }

    @GetMapping("/get/{id}") // ?
    public ResponseEntity<?> getActionById(@PathVariable long id) {
        try {
            Action action = actionsService.getActionById(id);
            if (action != null) {
                return ResponseEntity.ok(action);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Action with ID " + id + " not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve action " + id + ": " + e.getMessage());
        }

    }

}
