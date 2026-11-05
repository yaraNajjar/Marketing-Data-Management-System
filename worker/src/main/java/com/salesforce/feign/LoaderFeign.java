package com.salesforce.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "FeignLoader", url= "${services.loaders.url:http://localhost:9091/controller_loader}")
public interface LoaderFeign {

    @GetMapping("/feign/metric") // loader
    public Double checkMetricInDB(@RequestParam("accountId") long accountId,
                                  @RequestParam("start") String start,
                                  @RequestParam("end") String end,
                                  @RequestParam("metric") String metric);
}

