package com.salesforce.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Action {

    private long id;
    private String name;
    private String createDate;
    private long accountId;
    private List<List<Integer>> conditions;
    private ActionType actionType;
    private String runOnTime;
    private Days runOnDay;
    private String notificationText;
    private String sendTo;
    private boolean deleted;
    private boolean enabled = true;
    private String lastUpdate;
    private String lastRun;
}
