package com.tsfn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerImpl {

	@Autowired
	private SmsSenderService smsSenderService;

	@Autowired
	private SmsSenderService SmsSenderService;

	@KafkaListener(topics = "TsofenTopic", groupId = "groupId")
	public void listen(String to ,String message) {
		// Send SMS
		SmsSenderService.sendSMS(to, "Message from TsofenTopic: " + message);
		System.out.println("SMS sent successfully for message: " + message);
	}

}
