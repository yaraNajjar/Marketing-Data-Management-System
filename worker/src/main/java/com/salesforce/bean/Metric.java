package com.salesforce.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Metric {
    private long id;
    private String name;
    private long accountId;
    private String metric;
    private int threshold;
    private int timeFrameHours;
}
