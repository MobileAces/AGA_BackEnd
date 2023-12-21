package com.project.awesomegroup.service;

import com.project.awesomegroup.dto.Team;
import com.project.awesomegroup.dto.User;
import com.project.awesomegroup.dto.alarm.Alarm;
import com.project.awesomegroup.dto.alarmdetail.AlarmDetail;
import com.project.awesomegroup.dto.alarmdetail.AlarmDetailRequest;
import com.project.awesomegroup.dto.alarmdetail.AlarmDetailResponse;
import com.project.awesomegroup.dto.teammember.TeamMember;
import com.project.awesomegroup.repository.AlarmDetailRepository;
import com.project.awesomegroup.repository.AlarmRepository;
import com.project.awesomegroup.repository.UserRepository;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AlarmDetailService {

    private final AlarmDetailRepository alarmDetailRepository;
    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;

    public List<AlarmDetail> selectAll() {
        return alarmDetailRepository.findAll();
    }

    public List<AlarmDetail> selectByAlarmId(Integer alarm_id) {
        return alarmDetailRepository.findByAlarmAlarmId(alarm_id);
    }

    public AlarmDetail selectByAlarmDetailId(Integer alarmDetail_id) {
        return alarmDetailRepository.findById(alarmDetail_id).get();
    }

    @Transactional
    public AlarmDetail insert(AlarmDetailRequest request) {
        //팀알람 조회
        Optional<Alarm> alarm = alarmRepository.findById(request.getAlarmId());
        //유저 조회
        Optional<User> user = userRepository.findById(request.getUserId());
        //개인알람 생성
        AlarmDetail alarmDetail = AlarmDetail.createAlarmDetail(request, alarm.get(), user.get());

        alarmDetailRepository.save(alarmDetail);
        return alarmDetail;
    }

}
