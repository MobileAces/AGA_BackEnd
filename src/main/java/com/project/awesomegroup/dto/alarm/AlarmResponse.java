package com.project.awesomegroup.dto.alarm;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlarmResponse {

    private Integer alarmId;

    private String alarmName;

    private String alarmDay;

    private Integer teamId;

    //==생성 메서드==//
    public static AlarmResponse createAlarmResponse(Alarm alarm) {
        AlarmResponse response = AlarmResponse.builder()
                .alarmId(alarm.getAlarmId())
                .alarmName(alarm.getAlarmName())
                .alarmDay(alarm.getAlarmDay())
                .teamId(alarm.getTeam().getTeamId()).build();
        return response;
    }

}
