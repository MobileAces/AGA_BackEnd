package com.project.awesomegroup.dto.alarm.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AlarmListResponse {

    @Schema(description = "상태 메세지", nullable = false, example = "Success")
    private String message;
    @Schema(description = "상태 코드", nullable = false, example = "200")
    private Integer code;
    @Schema(description = "데이터", nullable = true)
    private List<AlarmResponseDTO> dataList;

    //생성 메소드//
    public static AlarmListResponse createAlarmListResponse(String message, Integer code, List<AlarmResponseDTO> dataList){
        return AlarmListResponse.builder()
                .message(message)
                .code(code)
                .dataList(dataList)
                .build();
    }
}
