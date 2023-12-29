package com.project.awesomegroup.dto.wakeup;

import com.project.awesomegroup.dto.alarm.Alarm;
import com.project.awesomegroup.dto.alarmdetail.AlarmDetail;
import com.project.awesomegroup.dto.team.Team;
import com.project.awesomegroup.dto.user.User;
import com.project.awesomegroup.dto.wakeup.request.WakeupSaveRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "wakeup")
public class Wakeup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wakeup_id")
    private Integer wakeupId;

    @Column
    private boolean success;

    @Column
    private Date datetime;

    @Column
    private Integer wakeupHour;

    @Column
    private Integer wakeupMinute;

    @Column
    private boolean wakeupForecast;

    @Column
    private boolean wakeupVoice;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "alarm_id")
    private Alarm alarm;

    @ManyToOne
    @JoinColumn(name = "alarm_detail_id")
    private AlarmDetail alarmDetail;

    //생성 메서드
    public static Wakeup createWakeup (WakeupSaveRequest request, User user, Team team, Alarm alarm, AlarmDetail alarmDetail) {
        return Wakeup.builder()
                .success(request.isSuccess())
                .datetime(request.getDatetime())
                .wakeupHour(request.getWakeupHour())
                .wakeupMinute(request.getWakeupMinute())
                .wakeupForecast(request.isWakeupForecast())
                .wakeupVoice(request.isWakeupVoice())
                .user(user)
                .team(team)
                .alarm(alarm)
                .alarmDetail(alarmDetail)
                .build();
    }
}
