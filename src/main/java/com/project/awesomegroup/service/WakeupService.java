package com.project.awesomegroup.service;

import com.project.awesomegroup.dto.alarm.Alarm;
import com.project.awesomegroup.dto.alarmdetail.AlarmDetail;
import com.project.awesomegroup.dto.team.Team;
import com.project.awesomegroup.dto.teammember.TeamMember;
import com.project.awesomegroup.dto.user.User;
import com.project.awesomegroup.dto.user.UserStatistics;
import com.project.awesomegroup.dto.wakeup.Wakeup;
import com.project.awesomegroup.dto.wakeup.WakeupDTO;
import com.project.awesomegroup.dto.wakeup.request.WakeupSaveRequest;
import com.project.awesomegroup.dto.wakeup.response.WakeupResponse;
import com.project.awesomegroup.dto.wakeup.response.WakeupResponseDTO;
import com.project.awesomegroup.dto.wakeup.response.WakeupStatisticsResponse;
import com.project.awesomegroup.dto.wakeup.response.WakeupStatisticsResponseDTO;
import com.project.awesomegroup.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WakeupService {

    private static final Logger logger = LoggerFactory.getLogger(WakeupService.class);

    @Autowired
    WakeupRepository wakeupRepository;

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final AlarmRepository alarmRepository;
    private final AlarmDetailRepository alarmDetailRepository;


    public WakeupResponse registerWakeup(WakeupSaveRequest request) {
        // 개인알람 조회
        Optional<AlarmDetail> alarmDetail = alarmDetailRepository.findById(request.getAlarmDetailId());
        if(!alarmDetail.isPresent()) {
            //개인 알람을 찾지 못했을 때 (code = 404)
            return WakeupResponse.createWakeupResponse("AlarmDetail not Found", 404, null);
        }
        //유저 조회
        Optional<User> user = userRepository.findById(request.getUserId());
        if(!user.isPresent()) {
            //유저를 찾지 못했을 때 (code = 404)
            return WakeupResponse.createWakeupResponse("User not Found", 404, null);
        }
        if(user.get().getUserId() == alarmDetail.get().getUser().getUserId()) {
            //팀알람 조회
            Optional<Alarm> alarm = alarmRepository.findById(alarmDetail.get().getAlarm().getAlarmId());
            if(!alarm.isPresent()) {
                //팀알람을 찾지 못했을 때 (code = 404)
                return WakeupResponse.createWakeupResponse("Alarm not Found", 404, null);
            }
            if(alarm.get().getAlarmId() == request.getAlarmId()) {
                // 팀 조회
                Optional<Team> team = teamRepository.findById(alarm.get().getTeam().getTeamId());
                if(!team.isPresent()) {
                    //팀을 찾지 못했을 때 (code = 404)
                    return WakeupResponse.createWakeupResponse("Team not Found", 404, null);
                }
                if(team.get().getTeamId() == request.getTeamId()) {
                    //팀멤버 조회
                    Optional<TeamMember> teamMember = teamMemberRepository.findByTeamTeamIdAndUserUserId(alarm.get().getTeam().getTeamId(), user.get().getUserId());
                    if(!teamMember.isPresent()) {
                        //팀에 속한 유저를 찾지 못했을 때 (code = 404)
                        return WakeupResponse.createWakeupResponse("User does not exist in the Team", 404, null);
                    }
                    //wakeup 생성
                    Wakeup wakeup = Wakeup.createWakeup(request, user.get(), team.get(), alarm.get(), alarmDetail.get());
                    //wakeup 저장
                    Wakeup savedWakeup = wakeupRepository.save(wakeup);

                    return WakeupResponse.createWakeupResponse("Save Success", 201, WakeupResponseDTO.createWakeupResponseDTO(savedWakeup));
                } else {
                    return WakeupResponse.createWakeupResponse("No match teamId", 409, null);
                }
            } else {
                return WakeupResponse.createWakeupResponse("No match alamId", 409, null);
            }
        } else {
            return WakeupResponse.createWakeupResponse("No match userId", 409, null);
        }
    }

    public List<WakeupDTO> getWakeupStatusByTeamAndDate(Integer teamId, Date date) {
        // 날짜를 기준으로 (하루) 조회
        Date startDate = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
        Date endDate = DateUtils.addDays(startDate, 1);

        // 해당 팀에 속한 멤버들의 wakeup 상태를 반환
        List<Wakeup> wakeupList = wakeupRepository.findByDatetimeAfterAndDatetimeBeforeAndTeam_TeamId(startDate, endDate, teamId);

        // 엔터티를 DTO로 변환
        return wakeupList.stream()
                .map(WakeupDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public ResponseEntity<WakeupStatisticsResponse> getWakeupStatisticsByTeamAndDateRange(List<String> nicknames, Integer teamId, Date startDate, Date endDate) {
        logger.info("startDate: " + startDate + ", endDate: " + endDate);

        // 팀 조회
        Optional<Team> team = teamRepository.findById(teamId);
        if(!team.isPresent()) {
            //팀을 찾지 못했을 때 (code = 404)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        //유저 조회
        List<User> userList = new ArrayList<>();
        for(String nickname : nicknames){
            Optional<User> user = userRepository.findByUserNickname(nickname);
            if(!user.isPresent()) {
                //유저를 찾지 못했을 때 (code = 404)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        }
        //팀에 속한 유저에 대한 유효성 검사를 해주면 좋음

        // 해당 팀에 속한 멤버들의 wakeup 상태를 조회
        List<Wakeup> wakeupList = wakeupRepository.findByDatetimeAfterAndDatetimeBeforeAndTeam_TeamId(startDate, endDate, teamId);
        // 맵 선언
        Map<String, Integer> result = new HashMap<>();

        for (Wakeup wakeup : wakeupList) {
            //개인 통계
            String currentUserNickname = wakeup.getUser().getUserNickname();
            //각자의 총 횟수
            result.put(currentUserNickname, result.getOrDefault(currentUserNickname, 0) + 1) ;

            //각자의 성공 횟수
            if(wakeup.isSuccess()) {
                result.put(currentUserNickname +"Success", result.getOrDefault(currentUserNickname +"Success", 0) + 1) ;
            }
            //명단에 존재하지 않는 유저 삭제
            for(String nickname : nicknames) {
                if(!result.keySet().contains(nickname)){
                    result.remove(nickname);
                    result.remove(nickname + "Success");
                }
            }
        }
        //총합 계산
        int userAlarmDetailTotal = 0;
        int userAlarmDetailSuccess = 0;
        for(String nickname : result.keySet()) {
            userAlarmDetailTotal += result.getOrDefault(nickname, 0);
            userAlarmDetailSuccess += result.getOrDefault(nickname + "Success", 0);
        }
        result.put("userAlarmDetailTotal", userAlarmDetailTotal - userAlarmDetailSuccess);
        result.put("userAlarmDetailSuccess", userAlarmDetailSuccess);

        //response 생성
        List<UserStatistics> userStatisticsList = new ArrayList<>();
        for (String nickname: nicknames) {
            userStatisticsList.add(UserStatistics.builder()
                    .nickname(nickname)
                    .totalSum(result.get(nickname))
                    .totalSuccessSum(result.get(nickname + "Success"))
                    .build());
        }

        WakeupStatisticsResponse wakeupResponse = WakeupStatisticsResponse.createWakeupResponse("Success", 200, WakeupStatisticsResponseDTO.createWakeupResponseDTO(
                userStatisticsList, result.get("userAlarmDetailTotal"), result.get("userAlarmDetailSuccess")
        ));
        return ResponseEntity.ok(wakeupResponse);
    }
}
