package com.project.awesomegroup.dto.alarmdetail.response;


import com.project.awesomegroup.dto.alarm.response.AlarmResponseDTO;
import com.project.awesomegroup.dto.alarmdetail.AlarmDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlarmDetailResponse {

    @Schema(description = "상태 메세지", nullable = false, example = "Success")
    private String message;
    @Schema(description = "상태 코드", nullable = false, example = "200")
    private Integer code;
    @Schema(description = "데이터", nullable = true)
    private AlarmDetailResponseDTO data;

    //생성 메소드//
    public static AlarmDetailResponse createAlarmResponse(String message, Integer code, AlarmDetailResponseDTO data){
        return AlarmDetailResponse.builder()
                .message(message)
                .code(code)
                .data(data)
                .build();
    }
}
