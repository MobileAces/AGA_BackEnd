package com.project.awesomegroup.dto.alarm.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AlarmListWithDetailResponse {

    @Schema(description = "상태 메세지", nullable = false, example = "Success")
    private String message;
    @Schema(description = "상태 코드", nullable = false, example = "200")
    private Integer code;
    @Schema(description = "데이터", nullable = true)
    private List<AlarmResponseWithDetailDTO> dataList;

    //생성 메소드//
    public static AlarmListWithDetailResponse createAlarmListWithDetailResponse(String message, Integer code, List<AlarmResponseWithDetailDTO> dataList){
        return AlarmListWithDetailResponse.builder()
                .message(message)
                .code(code)
                .dataList(dataList)
                .build();
    }
}
