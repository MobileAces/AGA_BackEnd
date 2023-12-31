package com.project.awesomegroup.dto.wakeup;

import com.project.awesomegroup.dto.alarm.Alarm;
import com.project.awesomegroup.dto.alarmdetail.AlarmDetail;
import com.project.awesomegroup.dto.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WakeupDTO {

    private Integer wakeupId;
    private boolean success;
    private Date datetime;
    private Integer wakeupHour;
    private Integer wakeupMinute;
    private boolean wakeupForecast;
    private boolean wakeupVoice;
    private User user;
    private Alarm alarm;
    private AlarmDetail alarmDetail;

    public static WakeupDTO fromEntity(Wakeup wakeup) {
        return WakeupDTO.builder()
                .wakeupId(wakeup.getWakeupId())
                .success(wakeup.isSuccess())
                .datetime(wakeup.getDatetime())
                .wakeupHour(wakeup.getWakeupHour())
                .wakeupMinute(wakeup.getWakeupMinute())
                .wakeupForecast(wakeup.isWakeupForecast())
                .wakeupVoice(wakeup.isWakeupVoice())
                .user(wakeup.getUser())
                .alarm(wakeup.getAlarm())
                .alarmDetail(wakeup.getAlarmDetail())
                .build();
    }

    public Wakeup toEntity() {
        return Wakeup.builder()
                .wakeupId(this.wakeupId)
                .success(this.success)
                .datetime(this.datetime)
                .wakeupHour(this.wakeupHour)
                .wakeupMinute(this.wakeupMinute)
                .wakeupForecast(this.wakeupForecast)
                .wakeupVoice(this.wakeupVoice)
                .user(this.user)
                .alarm(this.alarm)
                .alarmDetail(this.alarmDetail)
                .build();
    }
}
