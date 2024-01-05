package com.project.awesomegroup.service;

import com.project.awesomegroup.dto.alarm.Alarm;
import com.project.awesomegroup.dto.alarm.request.AlarmRequest;
import com.project.awesomegroup.dto.alarm.request.AlarmUpdateRequest;
import com.project.awesomegroup.dto.alarm.response.*;
import com.project.awesomegroup.dto.alarmdetail.response.AlarmDetailResponseDTO;
import com.project.awesomegroup.dto.team.Team;
import com.project.awesomegroup.repository.AlarmRepository;
import com.project.awesomegroup.repository.TeamRepository;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.json.HTTPTokener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final TeamRepository teamRepository;

    public ResponseEntity<AlarmListWithDetailResponse> findByTeamId(Integer teamId) {
        List<AlarmResponseWithDetailDTO> alarmList = new ArrayList<>();
        alarmRepository.findByTeamTeamId(teamId).forEach(e ->
                alarmList.add(AlarmResponseWithDetailDTO.builder()
                    .alarmId(e.getAlarmId())
                    .alarmName(e.getAlarmName())
                    .alarmDay(e.getAlarmDay())
                    .teamId(e.getTeam().getTeamId())
                    .dataList(e.getAlarmDetailList().stream().map(AlarmDetailResponseDTO::createAlarmDetailResponseDTO).collect(Collectors.toList()))
                    .build())
        );
        if(alarmList.isEmpty()) {
            //정보를 찾지 못했을 때 (code = 404)
            AlarmListWithDetailResponse response = AlarmListWithDetailResponse.createAlarmListWithDetailResponse("Alarm not Found", 404, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        //해당하는 ID 정보를 찾았을 때 (code = 200)
        AlarmListWithDetailResponse response = AlarmListWithDetailResponse.createAlarmListWithDetailResponse("Alarm Found", 200, alarmList);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<AlarmListResponse> findByTeamIdAndAlarmDay(Integer teamId, String day) {
        List<AlarmResponseDTO> alarmList = new ArrayList<>();
        alarmRepository.findByTeamTeamIdAndAlarmDayContaining(teamId, day).forEach(e ->
                alarmList.add(AlarmResponseDTO.builder()
                        .alarmId(e.getAlarmId())
                        .alarmName(e.getAlarmName())
                        .alarmDay(e.getAlarmDay())
                        .teamId(e.getTeam().getTeamId())
                        .build())
        );
        if(alarmList.isEmpty()) {
            //정보를 찾지 못했을 때 (code = 404)
            AlarmListResponse response = AlarmListResponse.createAlarmListResponse("Alarm not Found", 404, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        //해당하는 ID 정보를 찾았을 때 (code = 200)
        AlarmListResponse response = AlarmListResponse.createAlarmListResponse("Alarm Found", 200, alarmList);
        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<AlarmResponse> insert(AlarmRequest request) {
        try {
            //팀 조회
            Optional<Team> findTeam = teamRepository.findById(request.getTeamId());
            if(!findTeam.isPresent()) {
                //알람 등록 실패 시 (팀이 존재하지 않음) (code = 404)
                AlarmResponse response = AlarmResponse.createAlarmResponse("Team not Found", 404, null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            //알람 생성
            Alarm newAlarm = Alarm.createAlarm(request, findTeam.get());

            //알람 생성 성공 (code = 201)
            Alarm savedAlarm = alarmRepository.save(newAlarm);
            AlarmResponse response = AlarmResponse.createAlarmResponse("Success", 201, AlarmResponseDTO.builder()
                    .alarmId(savedAlarm.getAlarmId())
                    .alarmName(savedAlarm.getAlarmName())
                    .alarmDay(savedAlarm.getAlarmDay())
                    .teamId(request.getTeamId())
                    .build());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            //알람 생성 실패 시 (code = 500)
            AlarmResponse response = AlarmResponse.createAlarmResponse("Server Error", 500, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Transactional
    public ResponseEntity<AlarmResponse> update(AlarmUpdateRequest request) {
        try {
            //팀 조회
            Optional<Team> team = teamRepository.findById(request.getTeamId());
            if(!team.isPresent()) {
                //알람 수정 실패 시 (팀이 존재하지 않음) (code = 404)
                AlarmResponse response = AlarmResponse.createAlarmResponse("Team not Found", 404, null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            //알람 조회
            Optional<Alarm> alarm = alarmRepository.findById(request.getAlarmId());
            if(!alarm.isPresent()) {
                //알람 수정 실패 시 (알람이 존재하지 않음) (code = 404)
                AlarmResponse response = AlarmResponse.createAlarmResponse("Alarm not Found", 404, null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            //알람 정보 업데이트
            if(request.getAlarmName() != null){alarm.get().setAlarmName(request.getAlarmName());}
            if(request.getAlarmDay() != null){alarm.get().setAlarmDay(request.getAlarmDay());}
            AlarmResponse response = AlarmResponse.createAlarmResponse("Success", 200, AlarmResponseDTO.builder()
                    .alarmId(request.getAlarmId())
                    .alarmName(alarm.get().getAlarmName())
                    .alarmDay(alarm.get().getAlarmDay())
                    .teamId(request.getTeamId())
                    .build());
            return ResponseEntity.ok(response);
        }catch (PersistenceException e){
            //알람 생성 실패 시 (code = 500)
            AlarmResponse response = AlarmResponse.createAlarmResponse("Server Error", 500, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Transactional
    public ResponseEntity<AlarmDeleteResponse> delete(Integer alarmId) {
        //알람 조회
        Optional<Alarm> findAlarm = alarmRepository.findById(alarmId);
        if (!findAlarm.isPresent()) {
            //팀멤버로 등록된 정보가 존재하지 않을 때 (code = 404)
            AlarmDeleteResponse response = AlarmDeleteResponse.createAlarmDeleteResponse("Alarm not Found", 404, AlarmBooleanDTO.createAlarmBooleanDTO(false));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        //팀 조회
        Optional<Team> team = teamRepository.findById(findAlarm.get().getTeam().getTeamId());
        if(!team.isPresent()) {
            //알람 삭제 실패 시 (팀이 존재하지 않음) (code = 404)
            AlarmDeleteResponse response = AlarmDeleteResponse.createAlarmDeleteResponse("Team not Found", 404, AlarmBooleanDTO.createAlarmBooleanDTO(false));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        //팀멤버로 등록되었을 때 (code = 200)
        alarmRepository.delete(findAlarm.get());
        AlarmDeleteResponse response = AlarmDeleteResponse.createAlarmDeleteResponse("Success", 200, AlarmBooleanDTO.createAlarmBooleanDTO(true));
        return ResponseEntity.ok(response);
    }

}

