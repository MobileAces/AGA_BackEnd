package com.project.awesomegroup.dto.alarmdetail;

import com.project.awesomegroup.dto.User;
import com.project.awesomegroup.dto.alarm.Alarm;
import jakarta.persistence.*;
import lombok.*;

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
    private String alarmDetailForecast;

    @Column
    private String alarmDetailMemoVoice;

    @ManyToOne
    @JoinColumn(name = "alarm_id")
    private Alarm alarm;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
