package com.salesforce.metrics.controller;

import com.salesforce.metrics.bean.Metric;
import com.salesforce.metrics.controller.api.MetricControllerIFC;
import com.salesforce.metrics.service.MetricService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequestMapping("/metrics")
@CrossOrigin("*")
@RestController
public class MetricController implements MetricControllerIFC {

    @Autowired
    private MetricService metricService;


    @GetMapping("/active/all")
    public List<Metric> getAllActiveMetrics() {
        return metricService.getAllActiveMetrics();
    }

    @GetMapping("/history/all")
    public List<Metric> getAllHistoryMetrics() {
        return metricService.getAllHistoryMetrics();
    }

    @GetMapping("/active/{accountId}")
    public List<Metric> getMetricByAccountId(@PathVariable long accountId) {
        return metricService.getMetricByAccountId(accountId);
    }

    @GetMapping("/history/{accountId}")
    public List<Metric> getMetricHistoryByAccountId(@PathVariable long accountId) {
        return metricService.getMetricHistoryByAccountId(accountId);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createMetric(@RequestBody Metric metric) {
        try {
            String metricName = metricService.createMetric(metric);
            return ResponseEntity.ok("Metric with name " + metricName + " created successfully");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateMetric(@PathVariable long id, @RequestBody Metric updatedMetric) {
        try {
            String metricName = metricService.updateMetric(id, updatedMetric);
            return ResponseEntity.ok("Metric with name " + metricName + " updated successfully");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMetric(@PathVariable long id) {
        try {
            String metricName = metricService.deleteMetric(id);
            return ResponseEntity.status(HttpStatus.OK).body("Metric " + metricName + " delete successfully");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete metric : " + ex.getMessage());
        }
    }

    @PutMapping("/revive/{id}")
    public ResponseEntity<String> reviveMetric(@PathVariable long id) {
        try {
            String metricName = metricService.undeleteMetric(id);
            return ResponseEntity.ok("Metric name :  " + metricName + " restored successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }


}
