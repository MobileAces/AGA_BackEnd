package com.project.awesomegroup.dto;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}

