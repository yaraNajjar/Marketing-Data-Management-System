package com.salesforce.metrics.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "FeignAction", url = "${services.actions.url:http://localhost:9097/controller_action}")
public interface FeignAction {

    @GetMapping("/feign/{metricId}")//actions
    boolean metricUsingInAction(@PathVariable("metricId") long metricId);

}

