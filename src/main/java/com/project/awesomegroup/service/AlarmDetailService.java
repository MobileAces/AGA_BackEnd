package com.project.awesomegroup.service;

import com.project.awesomegroup.dto.alarm.response.AlarmListResponse;
import com.project.awesomegroup.dto.alarm.response.AlarmResponseDTO;
import com.project.awesomegroup.dto.alarmdetail.request.AlarmDetailUpdateRequest;
import com.project.awesomegroup.dto.alarmdetail.response.AlarmDetailListResponse;
import com.project.awesomegroup.dto.alarmdetail.response.AlarmDetailResponse;
import com.project.awesomegroup.dto.alarmdetail.response.AlarmDetailResponseDTO;
import com.project.awesomegroup.dto.team.Team;
import com.project.awesomegroup.dto.teammember.TeamMember;
import com.project.awesomegroup.dto.user.User;
import com.project.awesomegroup.dto.alarm.Alarm;
import com.project.awesomegroup.dto.alarmdetail.AlarmDetail;
import com.project.awesomegroup.dto.alarmdetail.request.AlarmDetailRequest;
import com.project.awesomegroup.repository.AlarmDetailRepository;
import com.project.awesomegroup.repository.AlarmRepository;
import com.project.awesomegroup.repository.TeamMemberRepository;
import com.project.awesomegroup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AlarmDetailService {

    private final Logger log = LoggerFactory.getLogger(AlarmDetailService.class);

    private final AlarmDetailRepository alarmDetailRepository;
    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;
    private final TeamMemberRepository teamMemberRepository;

    public List<AlarmDetail> selectAll() {
        return alarmDetailRepository.findAll();
    }

    public AlarmDetailListResponse selectByAlarmId(Integer alarm_id) {

        Optional<Alarm> alarmDetail = alarmRepository.findById(alarm_id);
        if(!alarmDetail.isPresent()) {
            //팀 알람을 찾지 못했을 때 (code = 404)
            return AlarmDetailListResponse.createAlarmResponse("Alarm not Found", 404, null);
        }

        List<AlarmDetailResponseDTO> alarmDetailList = alarmDetailRepository.findByAlarmAlarmId(alarm_id).stream()
                .map(AlarmDetailResponseDTO::createAlarmDetailResponseDTO)
                .collect(Collectors.toList());
        if(alarmDetailList.isEmpty()){
            //개인알람을 찾지 못했을 때 (code = 404)
            return AlarmDetailListResponse.createAlarmResponse("AlarmDetail not Found", 404, null);
        }
        //해당하는 alarmId 의 개인알람을 찾았을 때 (code = 200)
        return AlarmDetailListResponse.createAlarmResponse("AlarmDetail not Found", 200, alarmDetailList);
    }

    public AlarmDetailResponse selectByAlarmDetailId(Integer alarmDetail_id) {
        Optional<AlarmDetail> alarmDetail = alarmDetailRepository.findById(alarmDetail_id);
        if(!alarmDetail.isPresent()) {
            //개인알람을 찾지 못했을 때 (code = 404)
            return AlarmDetailResponse.createAlarmResponse("AlarmDetail not Found", 404, null);
        }
        //해당하는 ID 정보를 찾았을 때 (code = 200)
        return AlarmDetailResponse.createAlarmResponse("AlarmDetail Found", 200, AlarmDetailResponseDTO.createAlarmDetailResponseDTO(alarmDetail.get()));
    }

    @Transactional
    public AlarmDetailResponse insert(AlarmDetailRequest request) {
        //팀알람 조회
        Optional<Alarm> alarm = alarmRepository.findById(request.getAlarmId());
        if(!alarm.isPresent()) {
            //팀알람을 찾지 못했을 때 (code = 404)
            return AlarmDetailResponse.createAlarmResponse("Alarm not Found", 404, null);
        }
        //유저 조회
        Optional<User> user = userRepository.findById(request.getUserId());
        if(!user.isPresent()) {
            //유저를 찾지 못했을 때 (code = 404)
            return AlarmDetailResponse.createAlarmResponse("User not Found", 404, null);
        }
        //팀멤버 조회
        Optional<TeamMember> teamMember = teamMemberRepository.findByTeamTeamIdAndUserUserId(alarm.get().getTeam().getTeamId(), user.get().getUserId());
        if(!teamMember.isPresent()) {
            //팀에 속한 유저를 찾지 못했을 때 (code = 404)
            return AlarmDetailResponse.createAlarmResponse("User does not exist in the Team", 404, null);
        }
        //개인알람 생성
        AlarmDetail alarmDetail = AlarmDetail.createAlarmDetail(request, alarm.get(), user.get());
        alarmDetailRepository.save(alarmDetail);
        return AlarmDetailResponse.createAlarmResponse("Success", 201, AlarmDetailResponseDTO.createAlarmDetailResponseDTO(alarmDetail));
    }

    @Transactional
    public AlarmDetailResponse update(AlarmDetailUpdateRequest request) {
        try {
            //개인알람 조회
            Optional<AlarmDetail> alarmDetail = alarmDetailRepository.findById(request.getAlarmDetailId());
            if(!alarmDetail.isPresent()) {
                //팀알람을 찾지 못했을 때 (code = 404)
                return AlarmDetailResponse.createAlarmResponse("Alarm not Found", 404, null);
            }
            //유저 조회
            Optional<User> user = userRepository.findById(alarmDetail.get().getUser().getUserId());
            if(!user.isPresent()) {
                //유저를 찾지 못했을 때 (code = 404)
                return AlarmDetailResponse.createAlarmResponse("User not Found", 404, null);
            }
            //팀멤버 조회
            Optional<TeamMember> teamMember = teamMemberRepository.findByTeamTeamIdAndUserUserId(alarmDetail.get().getAlarm().getTeam().getTeamId(), user.get().getUserId());
            if(!teamMember.isPresent()) {
                //팀에 속한 유저를 찾지 못했을 때 (code = 404)
                return AlarmDetailResponse.createAlarmResponse("User does not exist in the Team", 404, null);
            }

            AlarmDetail findAlarmDetail = alarmDetailRepository.findById(request.getAlarmDetailId()).get();
            if(request.getAlarmDetailHour() >= 0 && request.getAlarmDetailHour() <= 23)
                findAlarmDetail.setAlarmDetailHour(request.getAlarmDetailHour());
            if(request.getAlarmDetailMinute() >= 0 && request.getAlarmDetailMinute() <= 59)
                findAlarmDetail.setAlarmDetailMinute(request.getAlarmDetailMinute());
            if(request.getAlarmDetailRetime() >= 0)
                findAlarmDetail.setAlarmDetailRetime(request.getAlarmDetailRetime());
            if(request.getAlarmDetailMemo() != null)
                findAlarmDetail.setAlarmDetailMemo(request.getAlarmDetailMemo());
            findAlarmDetail.setAlarmDetailForecast(request.isAlarmDetailForecast());
            findAlarmDetail.setAlarmDetailMemoVoice(request.isAlarmDetailMemoVoice());
            return AlarmDetailResponse.createAlarmResponse("Success", 200, AlarmDetailResponseDTO.createAlarmDetailResponseDTO(findAlarmDetail));
        } catch (Exception e) {
            return AlarmDetailResponse.createAlarmResponse("Server Error", 500, null);
        }
    }

    @Transactional
    public Map<String, String> delete(Integer alarmDetail_id) {
        Map<String, String> map = new HashMap<>();
        Optional<AlarmDetail> findAlarmDetail = alarmDetailRepository.findById(alarmDetail_id);
        if (!findAlarmDetail.isPresent()) {
            //개인알람이 존재하지 않을 때 (code = 404)
            map.put("result", "AlarmDetail not Found");
            return map;
        }

        //팀알람 조회
        Optional<Alarm> alarm = alarmRepository.findById(findAlarmDetail.get().getAlarm().getAlarmId());
        if(!alarm.isPresent()) {
            //개인알람 삭제 실패 시 (팀알람이 존재하지 않음) (code = 404)
            map.put("result", "Alarm not Found");
            return map;
        }

        //유저 조회
        Optional<User> user = userRepository.findById(findAlarmDetail.get().getUser().getUserId());
        if(!user.isPresent()) {
            //개인알람 삭제 실패 시 (유저가 존재하지 않음) (code = 404)
            map.put("result", "User not Found");
            return map;
        }

        //개인알람이 삭제되었을 때 (code = 200)
        alarmDetailRepository.delete(findAlarmDetail.get());
        map.put("result", "Success");
        return map;
    }
}
