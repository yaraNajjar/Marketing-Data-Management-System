package com.tsfn.loaders.controller;

import com.tsfn.loaders.controller.api.MarketingControllerIFC;
import com.tsfn.loaders.service.MarketingDataService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.tsfn.loaders.helper.FieldName.*;

@RestController
@AllArgsConstructor
@RequestMapping("/loader")
@CrossOrigin("*")
@EnableScheduling
public class MarketingDataController implements MarketingControllerIFC {

    @Autowired
    private MarketingDataService marketingDataService;

    @Scheduled(cron = "${scheduled.job.cronEveryHour}")
    public void scheduledScanFileFromGitHub() {
        List<String> providers = new ArrayList<>();
        providers.add(LINKEDIN.getFieldName());
        providers.add(FACEBOOK.getFieldName());
        providers.add(INSTAGRAM.getFieldName());
        for (String provider : providers) {
            marketingDataService.checkCSVFileProvidersDirectory(provider);
        }
    }

    @GetMapping("/trigger")
    public void triggerManual() {
        scheduledScanFileFromGitHub();
    }


}
























