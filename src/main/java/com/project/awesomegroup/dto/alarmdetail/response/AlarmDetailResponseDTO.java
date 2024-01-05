package com.project.awesomegroup.dto.alarmdetail.response;

import com.project.awesomegroup.dto.alarmdetail.AlarmDetail;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlarmDetailResponseDTO {
    private Integer alarmDetailId;

    private int alarmDetailHour;

    private int alarmDetailMinute;

    private int alarmDetailRetime;

    private String alarmDetailMemo;

    private boolean alarmDetailForecast;

    private boolean alarmDetailMemoVoice;

    private Integer alarmId;

    private String userId;

    private String userNickname;

    //==생성 메서드==//
    public static AlarmDetailResponseDTO createAlarmDetailResponseDTO(AlarmDetail alarmDetail) {
        AlarmDetailResponseDTO alarmDetailResponseDTO = AlarmDetailResponseDTO.builder()
                .alarmDetailId(alarmDetail.getAlarmDetailId())
                .alarmDetailHour(alarmDetail.getAlarmDetailHour())
                .alarmDetailMinute(alarmDetail.getAlarmDetailMinute())
                .alarmDetailRetime(alarmDetail.getAlarmDetailRetime())
                .alarmDetailMemo(alarmDetail.getAlarmDetailMemo())
                .alarmDetailForecast(alarmDetail.isAlarmDetailForecast())
                .alarmDetailMemoVoice(alarmDetail.isAlarmDetailMemoVoice())
                .alarmId(alarmDetail.getAlarm().getAlarmId())
                .userId(alarmDetail.getUser().getUserId())
                .userNickname(alarmDetail.getUser().getUserNickname())
                .build();
        return alarmDetailResponseDTO;
    }
}
