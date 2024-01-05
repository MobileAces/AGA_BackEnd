package com.project.awesomegroup.repository;

import com.project.awesomegroup.dto.alarm.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Integer> {
    List<Alarm> findByTeamTeamId(Integer teamId);
    List<Alarm> findByTeamTeamIdAndAlarmDayContaining(Integer teamId, String alarmDay);
}

