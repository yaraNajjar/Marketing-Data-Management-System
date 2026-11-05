package com.tsfn.loaders.repository;

import com.tsfn.loaders.bean.MarketingData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface MarketingDataRepository extends JpaRepository<MarketingData, Long> {

    @Query(value = "SELECT SUM(" +
            "CASE WHEN :columnName = 'impressions' THEN CAST(m.impressions AS DOUBLE) " +
            "     WHEN :columnName = 'views' THEN CAST(m.views AS DOUBLE) " +
            "     WHEN :columnName = 'clicks' THEN CAST(m.clicks AS DOUBLE) " +
            "     WHEN :columnName = 'ctr' THEN m.ctr " +
            "     WHEN :columnName = 'likes' THEN CAST(m.likes AS DOUBLE) " +
            "     WHEN :columnName = 'comments' THEN CAST(m.comments AS DOUBLE) " +
            "     WHEN :columnName = 'shares' THEN CAST(m.shares AS DOUBLE) " +
            "     WHEN :columnName = 'engagementRate' THEN m.engagement_rate " +
            "END) " +
            "FROM Marketing m " +
            "WHERE m.account_id = :accountId " +
            "AND m.timestamp <= :startTime AND m.timestamp >= :endTime " +
            "GROUP BY m.account_id " +
            "HAVING SUM(" +
            "CASE WHEN :columnName = 'impressions' THEN CAST(m.impressions AS DOUBLE) " +
            "     WHEN :columnName = 'views' THEN CAST(m.views AS DOUBLE) " +
            "     WHEN :columnName = 'clicks' THEN CAST(m.clicks AS DOUBLE) " +
            "     WHEN :columnName = 'ctr' THEN m.ctr " +
            "     WHEN :columnName = 'likes' THEN CAST(m.likes AS DOUBLE) " +
            "     WHEN :columnName = 'comments' THEN CAST(m.comments AS DOUBLE) " +
            "     WHEN :columnName = 'shares' THEN CAST(m.shares AS DOUBLE) " +
            "     WHEN :columnName = 'engagementRate' THEN m.engagement_rate " +
            "END)",
            nativeQuery = true)
    Double hasEnoughClicksByAccountIdAndTimeRange(
            @Param("accountId") long accountId,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            @Param("columnName") String columnName
    );


    int countByFileUploaded(String fileName);
}
