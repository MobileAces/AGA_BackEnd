package com.project.awesomegroup.dto.alarm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.awesomegroup.dto.team.Team;
import com.project.awesomegroup.dto.alarmdetail.AlarmDetail;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
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
}

