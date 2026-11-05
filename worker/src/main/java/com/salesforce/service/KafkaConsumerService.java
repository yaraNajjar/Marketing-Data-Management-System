package com.salesforce.service;

import com.salesforce.bean.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class KafkaConsumerService {

    @Autowired
    WorkerService workerService;

    @KafkaListener(topics = "ac", groupId = "Campaign", containerFactory = "actionListener")
    public void listen(Action action) {
        System.out.println("Received '" + action + "' from the AnimalTopic.");
        System.out.println(workerService.sendToKafka(action));
    }


}
