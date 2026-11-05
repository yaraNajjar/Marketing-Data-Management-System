package com.salesforce.service;


import com.salesforce.bean.Days;
import com.salesforce.bean.Action;
import com.salesforce.repository.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
public class JobsScheduler {

    @Autowired
    private ActionRepository actionRepository;
    @Autowired
    private KafkaProducerImpl kafkaProducerImpl;

    @Scheduled(fixedRate = 1800000)// 0.5 hour = 30 minutes = 1800000 milliseconds
    public void updateDatabase() {
        LocalDateTime currentTime = LocalDateTime.now();
        if (0 < currentTime.getMinute() && currentTime.getMinute() < 30) {
            currentTime = currentTime.withMinute(0);
        }
        if (30 < currentTime.getMinute() && currentTime.getMinute() < 60) {
            currentTime = currentTime.withMinute(30);
        }


        int dayOfWeek = (currentTime.getDayOfWeek().getValue()) % 7;
        dayOfWeek++;

        Days day = Days.fromValue(dayOfWeek);

        // Format the current time as AM/PM
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        String formattedTime = currentTime.format(formatter);
        System.out.println("Day = " + day);
        System.out.println("formattedTime = " + formattedTime);

        List<Action> actions = actionRepository.findScheduledActions(day, formattedTime);
        System.out.println("Size of action : " +actions.size());
        for (Action action : actions) {
            long actionId = action.getId();
            kafkaProducerImpl.sendMessage(action);
            updateLastRun(actionId);
        }
    }
    public void updateLastRun(long actionId) {
        Optional<Action> optionalAction = actionRepository.findById(actionId);
        optionalAction.ifPresent(action -> {
        Instant now = Instant.now();
        ZoneId israelTimeZone = ZoneId.of("Asia/Jerusalem");
        Timestamp currentTimestamp = Timestamp.from(now.atZone(israelTimeZone).toInstant());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSSS");
        action.setLastRun(dateFormat.format(currentTimestamp));
        actionRepository.save(action);
        });
    }
}