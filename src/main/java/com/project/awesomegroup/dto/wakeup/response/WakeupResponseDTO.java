package com.project.awesomegroup.dto.wakeup.response;

import com.project.awesomegroup.dto.wakeup.Wakeup;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class WakeupResponseDTO {

    private boolean success;

    private Date datetime;

    private Integer wakeupHour;

    private Integer wakeupMinute;

    private boolean wakeupForecast;

    private boolean wakeupVoice;

    private String userId;

    private Integer teamId;

    private Integer alarmId;

    private Integer alarmDetailId;

    public static WakeupResponseDTO createWakeupResponseDTO (Wakeup wakeup) {
        return WakeupResponseDTO.builder()
                .success(wakeup.isSuccess())
                .datetime(wakeup.getDatetime())
                .wakeupHour(wakeup.getWakeupHour())
                .wakeupMinute(wakeup.getWakeupMinute())
                .wakeupForecast(wakeup.isWakeupForecast())
                .wakeupVoice(wakeup.isWakeupVoice())
                .userId(wakeup.getUser().getUserId())
                .teamId(wakeup.getTeam().getTeamId())
                .alarmId(wakeup.getAlarm().getAlarmId())
                .alarmDetailId(wakeup.getAlarmDetail().getAlarmDetailId())
                .build();
    }
}
