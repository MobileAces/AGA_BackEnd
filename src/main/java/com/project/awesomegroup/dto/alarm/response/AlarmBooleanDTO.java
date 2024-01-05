package com.project.awesomegroup.dto.alarm.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlarmBooleanDTO {

    private Boolean result;

    //생성 메소드
    public static AlarmBooleanDTO createAlarmBooleanDTO(Boolean result){
        return AlarmBooleanDTO.builder()
                .result(result)
                .build();
    }
}
