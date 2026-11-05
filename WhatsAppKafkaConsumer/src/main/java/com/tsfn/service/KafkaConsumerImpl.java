package com.tsfn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerImpl {

    @Autowired
    private WhatsappSenderService whatsappSenderService;

    @KafkaListener(topics = "TsofenTopic", groupId = "groupId")
    public void listen(String to, String message) {
        // Send WhatsApp message
        whatsappSenderService.sendWhatsAppMessage("+972543330712", "Message from TsofenTopic: " + message); //  send WhatsApp Message
        System.out.println("WhatsApp message sent successfully for message: " + message);
    }
}
