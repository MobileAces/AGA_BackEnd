package com.project.awesomegroup.repository;

import com.project.awesomegroup.dto.alarmdetail.AlarmDetail;
import com.project.awesomegroup.dto.wakeup.Wakeup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AlarmDetailRepository extends JpaRepository<AlarmDetail, Integer> {
    List<AlarmDetail> findByAlarmAlarmId (Integer alarm_id);
    List<AlarmDetail> findByAlarm_AlarmIdAndUser_UserId(Integer alarmId, String userId);
}
