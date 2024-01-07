package com.project.awesomegroup.service;

import com.project.awesomegroup.dto.alarm.response.AlarmBooleanDTO;
import com.project.awesomegroup.dto.alarm.response.AlarmListResponse;
import com.project.awesomegroup.dto.alarm.response.AlarmResponseDTO;
import com.project.awesomegroup.dto.alarmdetail.request.AlarmDetailUpdateRequest;
import com.project.awesomegroup.dto.alarmdetail.response.*;
import com.project.awesomegroup.dto.team.Team;
import com.project.awesomegroup.dto.teammember.TeamMember;
import com.project.awesomegroup.dto.user.User;
import com.project.awesomegroup.dto.alarm.Alarm;
import com.project.awesomegroup.dto.alarmdetail.AlarmDetail;
import com.project.awesomegroup.dto.alarmdetail.request.AlarmDetailRequest;
import com.project.awesomegroup.dto.wakeup.Wakeup;
import com.project.awesomegroup.repository.AlarmDetailRepository;
import com.project.awesomegroup.repository.AlarmRepository;
import com.project.awesomegroup.repository.TeamMemberRepository;
import com.project.awesomegroup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<AlarmDetailListResponse> selectByAlarmId(Integer alarm_id) {

        Optional<Alarm> alarmDetail = alarmRepository.findById(alarm_id);
        if(alarmDetail.isEmpty()) {
            //팀 알람을 찾지 못했을 때 (code = 404)
            AlarmDetailListResponse response = AlarmDetailListResponse.createAlarmResponse("Alarm not Found", 404, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        List<AlarmDetailResponseDTO> alarmDetailList = alarmDetailRepository.findByAlarmAlarmId(alarm_id).stream()
                .map(AlarmDetailResponseDTO::createAlarmDetailResponseDTO)
                .collect(Collectors.toList());
        if(alarmDetailList.isEmpty()){
            //개인알람을 찾지 못했을 때 (code = 404)
            AlarmDetailListResponse response = AlarmDetailListResponse.createAlarmResponse("AlarmDetail not Found", 404, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        //해당하는 alarmId 의 개인알람을 찾았을 때 (code = 200)
        AlarmDetailListResponse response = AlarmDetailListResponse.createAlarmResponse("AlarmDetail Found", 200, alarmDetailList);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<AlarmDetailResponse> selectByAlarmDetailId(Integer alarmDetail_id) {
        Optional<AlarmDetail> alarmDetail = alarmDetailRepository.findById(alarmDetail_id);
        if(alarmDetail.isEmpty()) {
            //개인알람을 찾지 못했을 때 (code = 404)
            AlarmDetailResponse response = AlarmDetailResponse.createAlarmResponse("AlarmDetail not Found", 404, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        //해당하는 ID 정보를 찾았을 때 (code = 200)
        AlarmDetailResponse response = AlarmDetailResponse.createAlarmResponse("AlarmDetail Found", 200, AlarmDetailResponseDTO.createAlarmDetailResponseDTO(alarmDetail.get()));
        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<AlarmDetailResponse> insert(AlarmDetailRequest request) {
        //팀알람 조회
        Optional<Alarm> alarm = alarmRepository.findById(request.getAlarmId());
        if(alarm.isEmpty()) {
            //팀알람을 찾지 못했을 때 (code = 404)
            AlarmDetailResponse response = AlarmDetailResponse.createAlarmResponse("Alarm not Found", 404, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        //유저 조회
        Optional<User> user = userRepository.findById(request.getUserId());
        if(user.isEmpty()) {
            //유저를 찾지 못했을 때 (code = 404)
            AlarmDetailResponse response = AlarmDetailResponse.createAlarmResponse("User not Found", 404, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        //팀멤버 조회
        Optional<TeamMember> teamMember = teamMemberRepository.findByTeamTeamIdAndUserUserId(alarm.get().getTeam().getTeamId(), user.get().getUserId());
        if(teamMember.isEmpty()) {
            //팀에 속한 유저를 찾지 못했을 때 (code = 404)
            AlarmDetailResponse response = AlarmDetailResponse.createAlarmResponse("User does not exist in the Team", 404, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        //개인알람 생성
        AlarmDetail alarmDetail = AlarmDetail.createAlarmDetail(request, alarm.get(), user.get());
        alarmDetailRepository.save(alarmDetail);
        AlarmDetailResponse response = AlarmDetailResponse.createAlarmResponse("Success", 201, AlarmDetailResponseDTO.createAlarmDetailResponseDTO(alarmDetail));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Transactional
    public ResponseEntity<AlarmDetailResponse> update(AlarmDetailUpdateRequest request) {
        try {
            //개인알람 조회
            Optional<AlarmDetail> alarmDetail = alarmDetailRepository.findById(request.getAlarmDetailId());
            if(alarmDetail.isEmpty()) {
                //팀알람을 찾지 못했을 때 (code = 404)
                AlarmDetailResponse response = AlarmDetailResponse.createAlarmResponse("Alarm not Found", 404, null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            //유저 조회
            Optional<User> user = userRepository.findById(alarmDetail.get().getUser().getUserId());
            if(user.isEmpty()) {
                //유저를 찾지 못했을 때 (code = 404)
                AlarmDetailResponse response = AlarmDetailResponse.createAlarmResponse("User not Found", 404, null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            //팀멤버 조회
            Optional<TeamMember> teamMember = teamMemberRepository.findByTeamTeamIdAndUserUserId(alarmDetail.get().getAlarm().getTeam().getTeamId(), user.get().getUserId());
            if(teamMember.isEmpty()) {
                //팀에 속한 유저를 찾지 못했을 때 (code = 404)
                AlarmDetailResponse response = AlarmDetailResponse.createAlarmResponse("User does not exist in the Team", 404, null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
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
            findAlarmDetail.setAlarmDetailIsOn(request.isAlarmDetailIsOn());

            //개인알람 수정 성공 시 (code = 200)
            AlarmDetailResponse response = AlarmDetailResponse.createAlarmResponse("Success", 200, AlarmDetailResponseDTO.createAlarmDetailResponseDTO(findAlarmDetail));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            //서버 에러 (code = 500)
            AlarmDetailResponse response = AlarmDetailResponse.createAlarmResponse("Server Error", 500, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Transactional
    public ResponseEntity<AlarmDetailDeleteResponse> delete(Integer alarmDetail_id) {
        //개인알람 조회
        Optional<AlarmDetail> findAlarmDetail = alarmDetailRepository.findById(alarmDetail_id);
        if (findAlarmDetail.isEmpty()) {
            //개인알람이 존재하지 않을 때 (code = 404)
            AlarmDetailDeleteResponse response = AlarmDetailDeleteResponse.createAlarmDetailDeleteResponse("AlarmDetail not Found", 404, AlarmDetailBooleanDTO.createAlarmDetailBooleanDTO(false));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        //팀알람 조회
        Optional<Alarm> alarm = alarmRepository.findById(findAlarmDetail.get().getAlarm().getAlarmId());
        if(alarm.isEmpty()) {
            //개인알람 삭제 실패 시 (팀알람이 존재하지 않음) (code = 404)
            AlarmDetailDeleteResponse response = AlarmDetailDeleteResponse.createAlarmDetailDeleteResponse("Alarm not Found", 404, AlarmDetailBooleanDTO.createAlarmDetailBooleanDTO(false));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        //유저 조회
        Optional<User> user = userRepository.findById(findAlarmDetail.get().getUser().getUserId());
        if(user.isEmpty()) {
            //개인알람 삭제 실패 시 (유저가 존재하지 않음) (code = 404)
            AlarmDetailDeleteResponse response = AlarmDetailDeleteResponse.createAlarmDetailDeleteResponse("User not Found", 404, AlarmDetailBooleanDTO.createAlarmDetailBooleanDTO(false));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // AlarmDetail이 연관된 Wakeup 리스트 가져오기
        List<Wakeup> wakeupList = findAlarmDetail.get().getWakeupList();
        // 연관된 Wakeup 리스트 분리
        for (Wakeup wakeup : wakeupList) {
            wakeup.setAlarmDetail(null); // 연관 삭제
        }
        //개인알람이 삭제되었을 때 (code = 200)
        alarmDetailRepository.delete(findAlarmDetail.get());
        AlarmDetailDeleteResponse response = AlarmDetailDeleteResponse.createAlarmDetailDeleteResponse("Success", 200, AlarmDetailBooleanDTO.createAlarmDetailBooleanDTO(true));
        return ResponseEntity.ok(response);
    }
}
