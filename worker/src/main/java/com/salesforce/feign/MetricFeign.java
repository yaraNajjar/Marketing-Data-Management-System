package com.salesforce.feign;

import com.salesforce.bean.Metric;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "FeignMetric", url = "${services.metrics.url:http://localhost:9096/controller_metric}")
public interface MetricFeign {

    @GetMapping("/feign/{id}") // metrics
    Metric getMetricById(@PathVariable("id") long id);

}

