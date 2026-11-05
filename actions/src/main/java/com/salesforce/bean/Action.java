package com.salesforce.bean;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "actions")
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
