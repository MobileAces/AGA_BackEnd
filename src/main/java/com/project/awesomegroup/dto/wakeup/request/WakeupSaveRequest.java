package com.project.awesomegroup.dto.wakeup.request;

import com.project.awesomegroup.dto.alarm.Alarm;
import com.project.awesomegroup.dto.alarmdetail.AlarmDetail;
import com.project.awesomegroup.dto.team.Team;
import com.project.awesomegroup.dto.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class WakeupSaveRequest {

    private boolean success;

    private Date datetime;

    private Integer wakeupHour;

    private Integer wakeupMinute;

    private boolean wakeupForecast;

    private boolean wakeupVoice;

    private String userId;

    private Integer teamId;

    private Integer alarmId;

    private Integer alarmDetailId;
}
