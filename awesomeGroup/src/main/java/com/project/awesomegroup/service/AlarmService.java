package com.project.awesomegroup.service;

import com.project.awesomegroup.dto.alarm.Alarm;
import com.project.awesomegroup.repository.AlarmRepository;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;

    public List<Alarm> findByTeamId(Integer teamId) {
        return alarmRepository.findByTeamTeamId(teamId);
    }

    public List<Alarm> findByTeamIdAndAlarmDay(Integer teamId, String day) {
        return alarmRepository.findByTeamTeamIdAndAlarmDay(teamId, day);
    }

    @Transactional
    public Alarm insert(Alarm alarm) {
        alarmRepository.save(alarm);
        return alarm;
    }

    @Transactional
    public boolean update(Alarm alarm) {
        try {
            Alarm alarm1 = alarmRepository.findById(alarm.getAlarmId()).get();
            alarm1.setAlarmDay(alarm.getAlarmDay());
            alarm1.setAlarmName(alarm.getAlarmName());
            return true;
        }catch (PersistenceException e){
            return false;
        }
    }

    @Transactional
    public boolean delete(Integer alarm_id) {
        try{
            alarmRepository.deleteById(alarm_id);
            return true;
        } catch (Exception e){
            return false;
        }
    }

}

