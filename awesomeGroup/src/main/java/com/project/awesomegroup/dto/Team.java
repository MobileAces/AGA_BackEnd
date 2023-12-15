package com.project.awesomegroup.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name="team")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long teamId;

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

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TeamMember> teamMemberList = new ArrayList<>();

}