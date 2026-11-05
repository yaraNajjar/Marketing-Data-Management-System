package com.salesforce.service;


import com.salesforce.bean.NotificationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, NotificationMessage> kafkaTemplate;

    private final static String SMS_TOPIC = "smsTopics";
    private final static String EMAIL_TOPIC = "emailTopics";
    private final static String WHATSAPP_TOPIC = "whatsappTopics";

    public void sendMessage(NotificationMessage notificationMessage, String topicType) {
        switch (topicType) {
            case "SMS":
                kafkaTemplate.send(SMS_TOPIC, notificationMessage);
                break;
            case "WHATSAPP":
                kafkaTemplate.send(WHATSAPP_TOPIC, notificationMessage);
                break;
            case "EMAIL":
                kafkaTemplate.send(EMAIL_TOPIC, notificationMessage);
                break;
            default:
                System.out.println("No topic matching !");
        }
    }

}
