package com.salesforce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.salesforce.model.Action;
import com.salesforce.model.Days;

public interface ActionRepository extends JpaRepository<Action, Integer> {
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
