package com.project.awesomegroup.dto.alarmdetail.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AlarmDetailListResponse {

    @Schema(description = "상태 메세지", nullable = false, example = "Success")
    private String message;
    @Schema(description = "상태 코드", nullable = false, example = "200")
    private Integer code;
    @Schema(description = "데이터", nullable = true)
    private List<AlarmDetailResponseDTO> dataList;

    //생성 메소드//
    public static AlarmDetailListResponse createAlarmResponse(String message, Integer code, List<AlarmDetailResponseDTO> dataList){
        return AlarmDetailListResponse.builder()
                .message(message)
                .code(code)
                .dataList(dataList)
                .build();
    }
}
