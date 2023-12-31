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
import com.project.awesomegroup.dto.wakeup.response.*;
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

    public WakeupStatusResponse getWakeupStatusByTeamAndDate(Integer teamId, Date date) {
        // 날짜를 기준으로 (하루) 조회
        Date startDate = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
        Date endDate = DateUtils.addDays(startDate, 1);

        // 팀 조회
        Optional<Team> team = teamRepository.findById(teamId);
        if(!team.isPresent()) {
            //팀을 찾지 못했을 때 (code = 404)
            return WakeupStatusResponse.createWakeupStatusResponse("Team not Found", 404, null);
        }

        // 팀알람 조회
        List<Alarm> alarmList = alarmRepository.findByTeamTeamId(teamId);
        if(alarmList.isEmpty()) {
            //팀알람을 찾지 못했을 때 (code = 404)
            return WakeupStatusResponse.createWakeupStatusResponse("Alarm not Found", 404, null);
        }
        // 팀알람 등록
        Map<Integer, ArrayList<WakeupStatusResponseDTO>> resultMap = new HashMap<>();
        for(Alarm alarm : alarmList) {
            resultMap.put(alarm.getAlarmId(), new ArrayList<>());
        }

        // 해당 팀에 속한 멤버들의 wakeup 상태를 반환
        List<Wakeup> wakeupList = wakeupRepository.findByDatetimeAfterAndDatetimeBeforeAndTeam_TeamId(startDate, endDate, teamId);
        if(wakeupList.isEmpty()) {
            //상태가 존재하지 않을 때 (code = 404)
            return WakeupStatusResponse.createWakeupStatusResponse("Wakeup not Found", 404, null);
        }

        //WakeupStatusResponseDTO 리스트 생성
        List<WakeupStatusAlarmInfo> statusList = new ArrayList<>();
        //Map 안에서 각 wakeup에 해당하는 팀알람을 키로 리스트를 불러와서, wakeup을 WakeupStatusResponseDTO으로 변형하여 저장
        for(Wakeup wakeup : wakeupList) {
            resultMap.get(wakeup.getAlarm().getAlarmId())
                    .add(WakeupStatusResponseDTO.createWakeupStatusResponseDTO(wakeup));
        }

        //WakeupStatusAlarmInfo 리스트 생성
        List<WakeupStatusAlarmInfo> alrmInfoList = new ArrayList<>();
        //Map 안에서 각 wakeup에 해당하는 팀알람을 키로 리스트를 불러와서, wakeup을 WakeupStatusResponseDTO으로 변형하여 저장
        for(Alarm alarm : alarmList) {
            alrmInfoList.add(WakeupStatusAlarmInfo.createWakeupStatusResponseDTO(alarm.getAlarmId(), alarm.getAlarmName(), resultMap.get(alarm.getAlarmId())));
        }

        //response 생성
        return WakeupStatusResponse.createWakeupStatusResponse("Success", 200, alrmInfoList);
    }


    public ResponseEntity<WakeupStatisticsResponse> getWakeupStatisticsByTeamAndDateRange(List<String> nicknames, Integer teamId, Date startDate, Date endDate) {
        logger.info("startDate: " + startDate + ", endDate: " + endDate);

        // 팀 조회
        Optional<Team> team = teamRepository.findById(teamId);
        if(!team.isPresent()) {
            //팀을 찾지 못했을 때 (code = 404)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(WakeupStatisticsResponse.createWakeupResponse("Team not Found", 404, null));
        }
        //유저 조회
        List<User> userList = new ArrayList<>();
        for(String nickname : nicknames){
            Optional<User> user = userRepository.findByUserNickname(nickname);
            if(!user.isPresent()) {
                //유저를 찾지 못했을 때 (code = 404)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(WakeupStatisticsResponse.createWakeupResponse("User not Found", 404, null));
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
