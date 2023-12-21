package com.project.awesomegroup.service;

import com.project.awesomegroup.dto.Team;
import com.project.awesomegroup.dto.alarmdetail.AlarmDetail;
import com.project.awesomegroup.repository.AlarmDetailRepository;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AlarmDetailService {

    private final AlarmDetailRepository alarmDetailRepository;

    public List<AlarmDetail> selectAll() {
        return alarmDetailRepository.findAll();
    }

    public List<AlarmDetail> selectByAlarmId(Integer alarm_id) {
        return alarmDetailRepository.findByAlarmAlarmId(alarm_id);
    }

    public AlarmDetail selectByAlarmDetailId(Integer alarmDetail_id) {
        return alarmDetailRepository.findById(alarmDetail_id).get();
    }

}
