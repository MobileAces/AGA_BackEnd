package com.project.awesomegroup.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    private LocalDateTime teamCreateDate;

    @Column
    private String teamName;

    @Column
    private String teamPurpose;

    @Column
    private String teamInfo;

    @Column
    private String teamMaster;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TeamMember> teamMemberList = new ArrayList<>();

    //==연관관계 편의 메서드==//
    public void addTeamMember(TeamMember teamMember) {
        teamMemberList.add(teamMember);
        teamMember.setTeam(this);
    }

    //==생성 메서드==//
    public static Team createTeam(String teamName, String teamPurpose, String teamInfo, TeamMember teamMember) {
        Team team = new Team();
        team.setTeamCreateDate(LocalDateTime.now());
        team.setTeamName(teamName);
        team.setTeamPurpose(teamPurpose);
        team.setTeamInfo(teamInfo);
        team.setTeamMaster(teamMember.getUser().getUserId());
        team.addTeamMember(teamMember);
        return team;
    }
}