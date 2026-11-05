package com.salesforce.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.salesforce.model.Action;
import com.salesforce.model.Days;
import com.salesforce.repository.ActionRepository;

@Component
public class JobsScheduler {

	@Autowired
	private ActionRepository actionRepository;
	@Autowired
	private KafkaProducerImpl kafkaProducerImpl;

	@Scheduled(fixedRate = 1800000) // 0.5 hour = 30 minutes = 1800000 milliseconds
	public void updateDatabase() {
		LocalDateTime currentTime = LocalDateTime.now();
		if (0 < currentTime.getMinute() && currentTime.getMinute() < 30) {
			currentTime = currentTime.withMinute(0);
		}
		if (30 < currentTime.getMinute() && currentTime.getMinute() < 60) {
			currentTime = currentTime.withMinute(30);
		}

		// Determine the day of the week
		// MONDAY = 1 but we need MONDAY=2 so i added 1.
		int dayOfWeek = (currentTime.getDayOfWeek().getValue() + 1) % 7;

		Days day = Days.fromValue(dayOfWeek);

		// Format the current time as AM/PM
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
		String formattedTime = currentTime.format(formatter);

		List<Action> actions = actionRepository.findScheduledActions(day, formattedTime);

		for (Action action : actions) {
			Long actionId = action.getId();
			kafkaProducerImpl.sendMessage(action);
			updateLastRun(actionId);
		}
	}

	public void updateLastRun(long actionId) {
		Optional<Action> optionalAction = actionRepository.findById(actionId);
		optionalAction.ifPresent(action -> {
			Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
			action.setLastRun(currentTimestamp);
			actionRepository.save(action);
		});
	}

}
