package com.project.awesomegroup.repository;

import com.project.awesomegroup.dto.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Integer> {
    List<Alarm> findByTeamTeamId(Integer teamId);
    List<Alarm> findByTeamTeamIdAndAlarmDay(Integer teamId, String alarmDay);
}

