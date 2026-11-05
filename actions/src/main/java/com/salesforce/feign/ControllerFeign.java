package com.salesforce.feign;


import com.salesforce.service.ActionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/controller_action")
@CrossOrigin("*")
public class ControllerFeign {
    @Autowired
    private ActionsService actionsService;

    @GetMapping("/feign/{metricId}") // metrics
    public boolean checkBeforeDeleteMetrics(@PathVariable long metricId) {
        return actionsService.isUsing(metricId);
    }
    @GetMapping("/feign/security/{accountId}") // security
    public boolean removeAllActionsByAccountId(@PathVariable Long accountId) {
        return actionsService.deleteActionsByAccountId(accountId);
    }
}
