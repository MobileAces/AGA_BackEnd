package com.project.awesomegroup.dto.alarm.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlarmUpdateRequest {

    private Integer alarmId;

    private String alarmName;

    private String alarmDay;

    private Integer teamId;

}
