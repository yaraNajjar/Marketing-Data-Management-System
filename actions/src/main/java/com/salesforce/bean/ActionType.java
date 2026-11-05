package com.salesforce.bean;


import jakarta.persistence.Table;
import lombok.Getter;
import org.springframework.stereotype.Repository;

@Getter
@Table
public enum ActionType {
    SMS,
    WHATSAPP,
    EMAIL
}
