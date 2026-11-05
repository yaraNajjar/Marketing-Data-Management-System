package com.salesforce.controller;

import org.springframework.ai.chat.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Chat {

	@Autowired
	private ChatClient chatClient;

	@GetMapping("/test")
	public String generate(@RequestParam(value = "message", defaultValue = "Tell me a dad joke") String message) {
		// return chatClient.call(message);
		return message;
	}

}
