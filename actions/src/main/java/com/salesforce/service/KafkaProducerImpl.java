package com.salesforce.service;

import com.salesforce.bean.Action;
import com.salesforce.repository.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class KafkaProducerImpl {

    @Autowired
    private KafkaTemplate<String, Action> kafkaTemplate;

    private final static String TOPIC_NAME = "ac";
    @Autowired
    ActionRepository actionRepository;

//    @Autowired
//    private KafkaTemplate<String, Action> kafkaTemplate;

    public void sendMessage(Action a) {
        kafkaTemplate.send(TOPIC_NAME, a);
    }

    public Action loadActionById(long id) {
        return actionRepository.findById(id).orElseThrow(() -> new Error("Action not found"));
    }
}
