package com.project.awesomegroup.service;

import com.project.awesomegroup.dto.alarmdetail.AlarmDetail;
import com.project.awesomegroup.dto.wakeup.Wakeup;
import com.project.awesomegroup.dto.wakeup.WakeupDTO;
import com.project.awesomegroup.repository.AlarmDetailRepository;
import com.project.awesomegroup.repository.WakeupRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WakeupService {

    private static final Logger logger = LoggerFactory.getLogger(WakeupService.class);

    @Autowired
    WakeupRepository wakeupRepository;

    @Autowired
    AlarmDetailRepository alarmDetailRepository;

    public Wakeup registerWakeup(Wakeup wakeup) {
        return wakeupRepository.save(wakeup);
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

    public Map<String, Integer> getWakeupStatisticsByTeamAndDateRange(Integer teamId, Date startDate, Date endDate) {
            // 해당 팀에 속한 멤버들의 wakeup 상태를 조회
            List<Wakeup> wakeupList = wakeupRepository.findByDatetimeAfterAndDatetimeBeforeAndTeam_TeamId(startDate, endDate, teamId);

            // 조회된 Wakeup의 AlarmDetail 개수와 성공한 횟수를 계산
            int totalAlarmDetails = 0;
            int totalSuccessCount = 0;

            for (Wakeup wakeup : wakeupList) {
                List<AlarmDetail> alarmDetails = alarmDetailRepository.findByAlarm_AlarmIdAndUser_UserId(wakeup.getAlarm().getAlarmId(), wakeup.getUser().getUserId());
                totalAlarmDetails += alarmDetails.size();
                if (wakeup.isSuccess()) {
                    totalSuccessCount++;
                }
            }

            // 결과 반환
            Map<String, Integer> result = Map.of(
                    "totalAlarmDetails", totalAlarmDetails,
                    "totalSuccessCount", totalSuccessCount
            );

            return result;
        }
}
