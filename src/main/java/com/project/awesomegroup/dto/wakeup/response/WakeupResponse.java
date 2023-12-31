package com.project.awesomegroup.dto.wakeup.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WakeupResponse {

    @Schema(description = "상태 메세지", nullable = false, example = "Success")
    private String message;
    @Schema(description = "상태 코드", nullable = false, example = "200")
    private Integer code;
    @Schema(description = "데이터", nullable = true)
    private WakeupResponseDTO data;

    //생성 메서드
    public static WakeupResponse createWakeupResponse (String message, Integer code, WakeupResponseDTO data){
        return WakeupResponse.builder()
                .message(message)
                .code(code)
                .data(data)
                .build();
    }
}
