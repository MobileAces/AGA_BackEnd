package com.project.awesomegroup.dto.alarm.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlarmRequest {

    private String alarmName;

    @Schema(description = "요일", nullable = false, example = "MON,TUE,WED", type = "string")
    private String alarmDay;

    private Integer teamId;

}
