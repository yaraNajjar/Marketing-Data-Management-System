package com.salesforce.bean;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
public enum ActionType {
    SMS,
    WHATSAPP,
    EMAIL
}
