package com.project.awesomegroup.dto.alarmdetail;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlarmDetailRequest {
    private Integer alarmDetailId;

    private int alarmDetailHour;

    private int alarmDetailMinute;

    private int alarmDetailRetime;

    private String alarmDetailMemo;

    private String alarmDetailForecast;

    private String alarmDetailMemoVoice;

    private Integer alarmId;

    private String userId;

}
