package com.project.awesomegroup.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Table(name="team")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int teamId;

    @Column
    private Date teamCreateDate;

    @Column
    private String teamName;

    @Column
    private String teamPurpose;

    @Column
    private String teamInfo;

    @Column
    private int teamMaster;
}