package com.project.awesomegroup.dto.alarm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.awesomegroup.dto.alarm.request.AlarmRequest;
import com.project.awesomegroup.dto.alarm.response.AlarmResponse;
import com.project.awesomegroup.dto.team.Team;
import com.project.awesomegroup.dto.alarmdetail.AlarmDetail;
import com.project.awesomegroup.dto.teammember.TeamMember;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="alarm")
public class Alarm {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Integer alarmId;

    @Column
    private String alarmName;

    @Column
    private String alarmDay;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToMany(mappedBy = "alarm", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<AlarmDetail> alarmDetailList = new ArrayList<>();

    //==생성 메서드==//
    public static Alarm createAlarm(AlarmRequest request, Team team) {
        return Alarm.builder()
                .alarmName(request.getAlarmName())
                .alarmDay(request.getAlarmDay())
                .team(team)
                .build();
    }
}

