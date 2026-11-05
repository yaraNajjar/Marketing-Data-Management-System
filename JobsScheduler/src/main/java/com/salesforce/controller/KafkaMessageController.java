package com.salesforce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salesforce.model.Action;
import com.salesforce.service.KafkaProducerImpl;

import lombok.RequiredArgsConstructor;

@RequestMapping("/")
@RequiredArgsConstructor
@RestController
public class KafkaMessageController {

	@Autowired
	private KafkaProducerImpl kafkaProducerImpl;

	@PostMapping("/publish/{message}")
	public String publishMessage(@PathVariable("message") String message) {
		// kafkaProducerImpl.sendMessage(message);

		System.out.println("the message was published to Kafka successfully: " + message);

		return "the message was published to Kafka successfully:" + message + "to the TsofenTopic";
	}

	@GetMapping("/actions/{id}")
	public ResponseEntity<?> getActionById(@PathVariable Long id) {
		try {
			Action action = kafkaProducerImpl.loadActionById(id);
			kafkaProducerImpl.sendMessage(action);
			return new ResponseEntity<>(action, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

}
