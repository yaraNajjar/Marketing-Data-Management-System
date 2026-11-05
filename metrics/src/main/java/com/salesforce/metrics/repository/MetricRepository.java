package com.salesforce.metrics.repository;

import com.salesforce.metrics.bean.Metric;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MetricRepository extends JpaRepository<Metric, Integer> {

    List<Metric> findByAccountIdAndDeletedFalse(long accountId);
    List<Metric> findByAccountIdAndDeletedTrue(long accountId);

    List<Metric> findByDeletedTrue();
    List<Metric> findByDeletedFalse();


//    Metric findById(long id);
    Optional<Metric> findById(long id);



    Optional<Metric> findByAccountIdAndMetricAndThresholdAndTimeFrameHoursAndDeletedFalse(
            long AccountId,
            String metric,
            int threshold,
            int timeFrameHours);

}
