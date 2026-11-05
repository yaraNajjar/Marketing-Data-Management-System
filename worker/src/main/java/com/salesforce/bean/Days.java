package com.salesforce.bean;


import jakarta.persistence.Table;
import lombok.Getter;

@Getter
public enum Days {
    DAILY(0),
    SUNDAY(1),
    MONDAY(2),
    TUESDAY(3),
    WEDNESDAY(4),
    THURSDAY(5),
    FRIDAY(6),
    SATURDAY(7);

    private final int value;

    Days(int value) {
        this.value = value;
    }

}
