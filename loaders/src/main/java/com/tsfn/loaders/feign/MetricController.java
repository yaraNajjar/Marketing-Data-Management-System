package com.tsfn.loaders.feign;

import com.tsfn.loaders.service.MarketingDataService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/controller_loader")
@CrossOrigin("*")
public class MetricController {

    @Autowired
    private MarketingDataService marketingDataService;
    @GetMapping("/feign/metric")
    public Double checkMetricInDB(long accountId, String start, String end, String metric) {
        return marketingDataService.checkMetric(accountId, start, end, metric);
    }
}
