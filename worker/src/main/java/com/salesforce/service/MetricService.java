package com.salesforce.service;


import com.salesforce.bean.Metric;
import com.salesforce.feign.MetricFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MetricService {

    @Autowired
    MetricFeign metricFeign;

    public Metric getMetricById(long id){
        return metricFeign.getMetricById(id);
    }
}
