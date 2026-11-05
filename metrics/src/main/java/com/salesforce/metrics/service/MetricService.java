package com.salesforce.metrics.service;

import com.salesforce.metrics.bean.Metric;
import com.salesforce.metrics.feign.FeignAction;
import com.salesforce.metrics.helper.Helper;
import com.salesforce.metrics.repository.MetricRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class MetricService {

    @Autowired
    private MetricRepository metricRepository;

    @Autowired
    FeignAction feignAction;

    @Autowired
    private Helper helper;

    public List<Metric> getAllActiveMetrics() {
        return metricRepository.findByDeletedFalse();
    }

    public List<Metric> getAllHistoryMetrics() {
        return metricRepository.findByDeletedTrue();
    }

    public List<Metric> getMetricByAccountId(long accountId) {
        return metricRepository.findByAccountIdAndDeletedFalse(accountId);
    }

    public Optional<Metric> getMetricById(long id) {
        return metricRepository.findById(id);
    }

    public String createMetric(Metric metric) throws Exception {
        validationMetric(metric);
        LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(DateTimeFormatter.ISO_DATE);
        metric.setCreateDate(formattedDate);
        metricRepository.save(metric);
        return metric.getName();
    }

    public String updateMetric(long id, Metric metric) throws Exception {
        Metric existingMetric = metricRepository.findById(id)
                .orElseThrow(() -> new Exception("Action with ID " + id + " not found"));
        validationMetric(metric);
        existingMetric.setMetric(metric.getMetric());
        existingMetric.setName(metric.getName());
        existingMetric.setTimeFrameHours(metric.getTimeFrameHours());
        existingMetric.setThreshold(metric.getThreshold());
        metricRepository.save(existingMetric);
        return existingMetric.getName();
    }


    public List<Metric> getMetricHistoryByAccountId(long accountId) {
        return metricRepository.findByAccountIdAndDeletedTrue(accountId);
    }

    public String deleteMetric(Long id) throws Exception {
        Assert.notNull(id, "ID must not be null");
        Optional<Metric> optionalMetric = metricRepository.findById(id);
        if (!feignAction.metricUsingInAction(id)) {


            if (optionalMetric.isPresent()) {
                Metric metric = optionalMetric.get();
                metric.setDeleted(true);
                metricRepository.save(metric);
                return metric.getName();
            } else {
                throw new Exception("Metric with ID " + id + " does not exist");
            }
        }else{
            throw new Exception("Metric cannot be deleted because it is being used in an action. Please delete the action before attempting to delete the metric.");

        }
    }

    public String undeleteMetric(Long id) throws Exception {
        if (metricRepository.existsById(Math.toIntExact(id))) {
            Assert.notNull(id, "ID must not be null");
            Optional<Metric> optionalMetric = metricRepository.findById(id);
            if (optionalMetric.isPresent()) {
                Metric metric = optionalMetric.get();
                metric.setDeleted(false);
                metricRepository.save(metric);
                return metric.getName();
            } else {
                throw new Exception("Something wrong try again");
            }
        } else {
            throw new Exception("Action does not exist");
        }
    }


    private boolean similarMetricActiveExists(Metric metric) {
        Optional<Metric> existingAction = metricRepository.findByAccountIdAndMetricAndThresholdAndTimeFrameHoursAndDeletedFalse(
                metric.getAccountId(),
                metric.getMetric(),
                metric.getThreshold(),
                metric.getTimeFrameHours()

        );
        return existingAction.isPresent();
    }

    private void validationMetric(Metric metric) throws Exception {
        if (similarMetricActiveExists(metric)) {
            throw new Exception("Failed to create the metric. A metric with identical attributes already exists. Please try again with different values for (Metric / Threshold / Time Frame Hours).");
        }
        if (metric.getName() == null || metric.getName().isEmpty()) {
            throw new Exception("Failed : Input Name empty or not valid.");
        }
        if (metric.getThreshold() < 1) {
            throw new Exception("Failed : Input Threshold empty or not valid.");
        }
        if (!helper.getColumns().contains(metric.getMetric())) {
            throw new Exception("Failed : Input Metric type empty or not valid. " +
                    "Please choose one from : " +
                    "impressions/views/clicks/ctr/likes/comments/shares/engagementRate");
        }
        if (metric.getTimeFrameHours() < 1 || metric.getTimeFrameHours() > 24) {
            throw new Exception("Failed:  Input Time Frame Hours empty or not valid.");
        }
    }

    public boolean deleteMetricsByAccountId(long accountId) {
        try {
            List<Metric> metrics = metricRepository.findByAccountIdAndDeletedFalse(accountId);
            for (Metric m : metrics) {
                metricRepository.delete(m);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
