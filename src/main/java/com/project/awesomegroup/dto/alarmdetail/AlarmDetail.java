package com.project.awesomegroup.dto.alarmdetail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.awesomegroup.dto.alarmdetail.request.AlarmDetailRequest;
import com.project.awesomegroup.dto.user.User;
import com.project.awesomegroup.dto.alarm.Alarm;
import com.project.awesomegroup.dto.wakeup.Wakeup;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "alarmdetail")
public class AlarmDetail {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer alarmDetailId;

    @Column
    private int alarmDetailHour;

    @Column
    private int alarmDetailMinute;

    @Column
    private int alarmDetailRetime;

    @Column
    private String alarmDetailMemo;

    @Column
    private boolean alarmDetailForecast;

    @Column
    private boolean alarmDetailMemoVoice;

    @Column
    private boolean alarmDetailIsOn;

    @ManyToOne
    @JoinColumn(name = "alarm_id")
    private Alarm alarm;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "alarmDetail")
    @JsonIgnore
    private List<Wakeup> wakeupList = new ArrayList<>();

    //==생성 메서드==//
    public static AlarmDetail createAlarmDetail(AlarmDetailRequest request, Alarm alarm, User user) {
        AlarmDetail alarmDetail = AlarmDetail.builder()
                .alarmDetailHour(request.getAlarmDetailHour())
                .alarmDetailMinute(request.getAlarmDetailMinute())
                .alarmDetailRetime(request.getAlarmDetailRetime())
                .alarmDetailMemo(request.getAlarmDetailMemo())
                .alarmDetailForecast(request.isAlarmDetailForecast())
                .alarmDetailMemoVoice(request.isAlarmDetailMemoVoice())
                .alarmDetailIsOn(request.isAlarmDetailIsOn())
                .alarm(alarm)
                .user(user)
                .build();
        return alarmDetail;
    }
}
