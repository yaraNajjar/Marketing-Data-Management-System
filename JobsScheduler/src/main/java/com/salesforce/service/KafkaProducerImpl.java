package com.salesforce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.salesforce.model.Action;
import com.salesforce.repository.ActionRepository;

@Service
public class KafkaProducerImpl {

	@Autowired
	ActionRepository actionRepository;

	@Autowired
	private KafkaTemplate<String, Action> kafkaTemplate;

	public void sendMessage(Action a) {
		kafkaTemplate.send("TsofenTopic", a);
	}

	public Action loadActionById(long id) {
		return actionRepository.findById(id).orElseThrow(() -> new Error("Action not found"));
	}
}
