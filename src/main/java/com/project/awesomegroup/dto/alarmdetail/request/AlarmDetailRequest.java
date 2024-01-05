package com.project.awesomegroup.dto.alarmdetail.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlarmDetailRequest {
    private int alarmDetailHour;

    private int alarmDetailMinute;

    private int alarmDetailRetime;

    private String alarmDetailMemo;

    private boolean alarmDetailForecast;

    private boolean alarmDetailMemoVoice;

    private Integer alarmId;

    private String userId;

}
