package com.salesforce.metrics.feign;

import com.salesforce.metrics.bean.Metric;
import com.salesforce.metrics.service.MetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/controller_metric")
@CrossOrigin("*")
public class ControllerFeign {
    @Autowired
    private MetricService metricService;

    @GetMapping("/feign/security/{accountId}") // security using
    public boolean deleteMetricsByAccountId(@PathVariable Long accountId) {
        return metricService.deleteMetricsByAccountId(accountId);
    }

    @GetMapping("/feign/{id}") // worker using
    public Optional<Metric> getMetricById(@PathVariable long id) {
        return metricService.getMetricById(id);
    }
}
