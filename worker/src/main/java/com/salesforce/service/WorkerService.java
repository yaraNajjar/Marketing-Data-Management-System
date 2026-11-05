package com.salesforce.service;

import com.salesforce.bean.Action;
import com.salesforce.bean.ActionType;
import com.salesforce.bean.Metric;
import com.salesforce.bean.NotificationMessage;
import com.salesforce.feign.LoaderFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
public class WorkerService {


    @Autowired
    private MetricService metricService;

    @Autowired
    LoaderFeign loaderFeign;

    @Autowired
    KafkaProducerService kafkaProducerService;

    public String sendToKafka(Action action) {
        if (checkCondition(action.getConditions(), action.getLastRun())) {
            String sentTo = action.getSendTo();
            ActionType actionType = action.getActionType();
            String notificationText = action.getNotificationText();
            System.out.println("actionType   " + actionType);
            System.out.println("sentTo  " + sentTo);
            System.out.println("notificationText  " + notificationText);
            NotificationMessage notificationMessage = new NotificationMessage(sentTo, notificationText);
            kafkaProducerService.sendMessage(notificationMessage, String.valueOf(actionType));
            return "Success to send ";
        } else {
            System.out.println("Failure to send ");
            return "Failure to send ";
        }
    }

    public Metric getMetricById(long id) {
        return metricService.getMetricById(id);
    }

    public boolean checkCondition(List<List<Integer>> conditions, String start) {
        boolean result = false;
        for (List<Integer> sublist : conditions) {
            boolean sublistResult = true;

            for (Integer item : sublist) {
                sublistResult = sublistResult && checkMetric(getMetricById(item), start);
            }
            result = result || sublistResult;
        }
        return result;
    }

    public boolean checkMetric(Metric metric, String startTime) {
        String endTime = subtractHours(startTime, metric.getTimeFrameHours());
        Double result = loaderFeign.checkMetricInDB(
                metric.getAccountId(),
                startTime,
                endTime,
                metric.getMetric());
        System.out.println("result = " + result);
        return result != null && result >= metric.getThreshold();
    }

    public static String subtractHours(String dateTimeStr, int hours) {
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSS"));
        LocalDateTime newDateTime = dateTime.minusHours(hours);
        return newDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSS"));
    }
}
