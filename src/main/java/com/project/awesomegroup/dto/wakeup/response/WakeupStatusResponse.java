package com.project.awesomegroup.dto.wakeup.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WakeupStatusResponse {

    @Schema(description = "상태 메세지", nullable = false, example = "Success")
    private String message;
    @Schema(description = "상태 코드", nullable = false, example = "200")
    private Integer code;
    @Schema(description = "데이터", nullable = true)
    private List<WakeupStatusAlarmInfo> dataList;

    //생성 메서드
    public static WakeupStatusResponse createWakeupStatusResponse (String message, Integer code, List<WakeupStatusAlarmInfo> dataList){
        return WakeupStatusResponse.builder()
                .message(message)
                .code(code)
                .dataList(dataList)
                .build();
    }
}
