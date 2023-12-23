package com.project.awesomegroup.dto.alarmdetail;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlarmDetailResponse {
    private Integer alarmDetailId;

    private int alarmDetailHour;

    private int alarmDetailMinute;

    private int alarmDetailRetime;

    private String alarmDetailMemo;

    private String alarmDetailForecast;

    private String alarmDetailMemoVoice;

    private Integer alarmId;

    private String userId;

    //==생성 메서드==//
    public static AlarmDetailResponse createAlarmDetailResponse(AlarmDetail alarmDetail) {
        AlarmDetailResponse alarmDetailResponse = AlarmDetailResponse.builder()
                .alarmDetailId(alarmDetail.getAlarmDetailId())
                .alarmDetailHour(alarmDetail.getAlarmDetailHour())
                .alarmDetailMinute(alarmDetail.getAlarmDetailMinute())
                .alarmDetailRetime(alarmDetail.getAlarmDetailRetime())
                .alarmDetailMemo(alarmDetail.getAlarmDetailMemo())
                .alarmDetailForecast(alarmDetail.getAlarmDetailForecast())
                .alarmDetailMemoVoice(alarmDetail.getAlarmDetailMemoVoice())
                .alarmId(alarmDetail.getAlarm().getAlarmId())
                .userId(alarmDetail.getUser().getUserId())
                .build();
        return alarmDetailResponse;
    }
}
