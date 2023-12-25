package com.project.awesomegroup.dto.alarm.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlarmRequest {

    private String alarmName;

    private String alarmDay;

    private Integer teamId;

}
