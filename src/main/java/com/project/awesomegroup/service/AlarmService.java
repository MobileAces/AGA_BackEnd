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

    public AlarmListWithDetailResponse findByTeamId(Integer teamId) {
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
            return AlarmListWithDetailResponse.createAlarmListWithDetailResponse("Alarm not Found", 404, null);
        }
        //해당하는 ID 정보를 찾았을 때 (code = 200)
        return AlarmListWithDetailResponse.createAlarmListWithDetailResponse("Alarm Found", 200, alarmList);
    }

    public AlarmListResponse findByTeamIdAndAlarmDay(Integer teamId, String day) {

        List<AlarmResponseDTO> alarmList = new ArrayList<>();
        alarmRepository.findByTeamTeamIdAndAlarmDay(teamId, day).forEach(e ->
                alarmList.add(AlarmResponseDTO.builder()
                        .alarmId(e.getAlarmId())
                        .alarmName(e.getAlarmName())
                        .alarmDay(e.getAlarmDay())
                        .teamId(e.getTeam().getTeamId())
                        .build())
        );
        if(alarmList.isEmpty()) {
            //정보를 찾지 못했을 때 (code = 404)
            return AlarmListResponse.createAlarmListResponse("Alarm not Found", 404, null);
        }
        //해당하는 ID 정보를 찾았을 때 (code = 200)
        return AlarmListResponse.createAlarmListResponse("Alarm Found", 200, alarmList);
    }

    @Transactional
    public AlarmResponse insert(AlarmRequest request) {
        try {
            //팀 조회
            Optional<Team> findTeam = teamRepository.findById(request.getTeamId());
            if(!findTeam.isPresent()) {
                //알람 등록 실패 시 (팀이 존재하지 않음) (code = 404)
                return AlarmResponse.createAlarmResponse("Team not Found", 404, null);
            }
            //알람 생성
            Alarm newAlarm = Alarm.createAlarm(request, findTeam.get());

            //알람 생성 성공 (code = 201)
            alarmRepository.save(newAlarm);
            return AlarmResponse.createAlarmResponse("Success", 201, AlarmResponseDTO.builder()
                    .alarmName(request.getAlarmName())
                    .alarmDay(request.getAlarmDay())
                    .teamId(request.getTeamId())
                    .build());
        } catch (Exception e) {
            //알람 생성 실패 시 (code = 500)
            return AlarmResponse.createAlarmResponse("Server Error", 500, null);
        }
    }

    @Transactional
    public AlarmResponse update(AlarmUpdateRequest request) {
        try {
            //팀 조회
            Optional<Team> team = teamRepository.findById(request.getTeamId());
            if(!team.isPresent()) {
                //알람 수정 실패 시 (팀이 존재하지 않음) (code = 404)
                return AlarmResponse.createAlarmResponse("Team not Found", 404, null);
            }
            //알람 조회
            Optional<Alarm> alarm = alarmRepository.findById(request.getAlarmId());
            if(!alarm.isPresent()) {
                //알람 수정 실패 시 (알람이 존재하지 않음) (code = 404)
                return AlarmResponse.createAlarmResponse("Alarm not Found", 404, null);
            }
            //알람 정보 업데이트
            if(request.getAlarmName() != null){alarm.get().setAlarmName(request.getAlarmName());}
            if(request.getAlarmDay() != null){alarm.get().setAlarmDay(request.getAlarmDay());}
            return AlarmResponse.createAlarmResponse("Success", 200, AlarmResponseDTO.builder()
                    .alarmId(request.getAlarmId())
                    .alarmName(alarm.get().getAlarmName())
                    .alarmDay(alarm.get().getAlarmDay())
                    .teamId(request.getTeamId())
                    .build());
        }catch (PersistenceException e){
            //알람 생성 실패 시 (code = 500)
            return AlarmResponse.createAlarmResponse("Server Error", 500, null);
        }
    }

    @Transactional
    public Map<String, String> delete(Integer alarmId) {
        Map<String, String> map = new HashMap<>();
        Optional<Alarm> findAlarm = alarmRepository.findById(alarmId);
        if (!findAlarm.isPresent()) {
            //팀멤버로 등록된 정보가 존재하지 않을 때 (code = 404)
            map.put("result", "Alarm not Found");
            return map;
        }

        //팀 조회
        Optional<Team> team = teamRepository.findById(findAlarm.get().getTeam().getTeamId());
        if(!team.isPresent()) {
            //알람 삭제 실패 시 (팀이 존재하지 않음) (code = 404)
            map.put("result", "Team not Found");
            return map;
        }

        //팀멤버로 등록되었을 때 (code = 200)
        alarmRepository.delete(findAlarm.get());
        map.put("result", "Success");
        return map;
    }

}

