package com.project.awesomegroup.dto.alarm.response;

import com.project.awesomegroup.dto.alarm.Alarm;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlarmResponseDTO {

    private Integer alarmId;

    private String alarmName;

    private String alarmDay;

    private Integer teamId;

}
