package com.project.awesomegroup.dto.alarmdetail.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlarmDetailBooleanDTO {

    private Boolean result;

    //생성 메소드
    public static AlarmDetailBooleanDTO createAlarmDetailBooleanDTO(Boolean result){
        return AlarmDetailBooleanDTO.builder()
                .result(result)
                .build();
    }
}
