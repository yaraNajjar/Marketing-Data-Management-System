package com.salesforce.model;

import jakarta.persistence.Table;

@Table
public enum ActionType {
	SMS, WHATSAPP, EMAIL
}