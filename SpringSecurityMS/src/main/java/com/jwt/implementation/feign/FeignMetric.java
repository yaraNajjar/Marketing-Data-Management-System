package com.jwt.implementation.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "FeignMetric", url = "${services.metrics.url:http://localhost:9096/controller_metric}")

public interface FeignMetric {

    @GetMapping("/feign/security/{accountId}")
    boolean deleteUserAndMetrics(@PathVariable("accountId") Long accountId);

}
