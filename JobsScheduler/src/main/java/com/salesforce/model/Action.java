package com.salesforce.model;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "actions_data")
public class Action {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private Timestamp createDate;
	private String createBy;
	private String conditions;
	private ActionType actionType;
	private String runOnTime;
	private Days runOnDay;
	private String notificationText;
	private String sendTo;
	private boolean deleted;
	private boolean enabled;
	private Timestamp lastUpdate;
	private Timestamp lastRun;
}
