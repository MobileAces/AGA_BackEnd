package com.project.awesomegroup.dto.alarm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.awesomegroup.dto.wather.Weather;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlarmResponseDTO {

    private Integer alarmId;

    private String alarmName;

    private String alarmDay;

    private Integer teamId;

    //==생성 메서드==//
    public static AlarmResponseDTO createAlarmResponse(Alarm alarm) {
        AlarmResponseDTO response = AlarmResponseDTO.builder()
                .alarmId(alarm.getAlarmId())
                .alarmName(alarm.getAlarmName())
                .alarmDay(alarm.getAlarmDay())
                .teamId(alarm.getTeam().getTeamId()).build();
        return response;
    }

}
