package com.project.awesomegroup.repository;

import com.project.awesomegroup.dto.wakeup.Wakeup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface WakeupRepository extends JpaRepository<Wakeup, Integer> {

    List<Wakeup> findByDatetimeAfterAndDatetimeBeforeAndTeam_TeamId(Date startDate, Date endDate, Integer teamId);

    List<Wakeup> findByTeam_TeamIdAndDatetimeStartingWith(Integer teamId, LocalDate date);
}
