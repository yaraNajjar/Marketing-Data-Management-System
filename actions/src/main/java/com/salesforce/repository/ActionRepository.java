package com.salesforce.repository;

import com.salesforce.bean.Action;
import com.salesforce.bean.ActionType;
import com.salesforce.bean.Days;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {
    List<Action> findAllActionsByAccountId(long accountId);

    Action findActionById(long id);

    List<Action> findByAccountIdAndDeletedFalse(long accountId);

    List<Action> findByAccountIdAndDeletedTrue(long accountId);

    List<Action> findAllByDeletedFalse();

    List<Action> findByDeletedTrue();

    List<Action> findActionsByAccountId(long accoutId);

    Optional<Action> findByAccountIdAndActionTypeAndRunOnTimeAndRunOnDayAndNotificationTextAndSendToAndDeletedAndEnabled(
            long AccountId,
            ActionType actionType,
            String runOnTime,
            Days runOnDay,
            String notificationText,
            String sendTo,
            boolean deleted,
            boolean enabled
    );

    // Since email is unique, we'll find users by email
    Optional<Action> findById(long id);

    @Query("SELECT a FROM Action a " + "WHERE (a.runOnDay = :day OR a.runOnDay = DAILY )" + "AND a.enabled = true "
            + "AND a.runOnTime = :formattedTime " + "AND a.deleted = false")
    List<Action> findScheduledActions(@Param("day") Days day, @Param("formattedTime") String formattedTime);

    @Transactional
    @Modifying
    @Query("UPDATE Action a SET a.runOnDay = 0 WHERE a.id = :id")
    void updateRunOnDayToZero(Long id);

}
