package com.project.awesomegroup.dto.wakeup.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.awesomegroup.dto.wakeup.Wakeup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class WakeupStatusResponseDTO {

    private String userNickname;

    private boolean success;

    @Schema(description = "기상 설정 날짜", nullable = false, example = "2023-12-01", type = "string")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date datetime;

    private Integer wakeupHour;

    private Integer wakeupMinute;

    private String wakeupMemo;

    private boolean wakeupForecast;

    private boolean wakeupVoice;

    public static WakeupStatusResponseDTO createWakeupStatusResponseDTO(Wakeup wakeup) {
        return WakeupStatusResponseDTO.builder()
                .userNickname(wakeup.getUser().getUserNickname())
                .success(wakeup.isSuccess())
                .datetime(wakeup.getDatetime())
                .wakeupHour(wakeup.getWakeupHour())
                .wakeupMinute(wakeup.getWakeupMinute())
                .wakeupMemo(wakeup.getWakeupMemo())
                .wakeupForecast(wakeup.isWakeupForecast())
                .wakeupVoice(wakeup.isWakeupVoice())
                .build();
    }

}
