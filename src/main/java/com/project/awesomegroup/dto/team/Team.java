package com.project.awesomegroup.dto.team;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.awesomegroup.dto.alarm.Alarm;
import com.project.awesomegroup.dto.invate.Invitation;
import com.project.awesomegroup.dto.teammember.TeamMember;
import com.project.awesomegroup.dto.wakeup.Wakeup;
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
@Table(name="team")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Integer teamId;

    @Column
    private LocalDateTime teamCreateDate;

    @Column
    private String teamName;

    @Column
    private String teamInfo;

    @Column
    private String teamMaster;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TeamMember> teamMemberList = new ArrayList<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Alarm> alarmList = new ArrayList<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Wakeup> wakeupList = new ArrayList<>();

    @OneToOne(mappedBy = "team", cascade = CascadeType.ALL)
    @JsonIgnore
    private Invitation invitation;

    //==연관관계 편의 메서드==//
    public void addTeamMember(TeamMember teamMember) {
        teamMemberList.add(teamMember);
        teamMember.setTeam(this);
    }

    public void addInvitation(Invitation invitation){
        this.invitation = invitation;
        invitation.setTeam(this);
    }

    //==생성 메서드==//
    public static Team createTeam(String teamName, String teamInfo, TeamMember teamMember) {
        Team team = new Team();
        team.setTeamCreateDate(LocalDateTime.now());
        team.setTeamName(teamName);
        team.setTeamInfo(teamInfo);
        team.setTeamMaster(teamMember.getUser().getUserId());
        team.addTeamMember(teamMember);
        return team;
    }

    public static Team createTeam(Integer teamId) {
        Team team = Team.builder()
                .teamId(teamId)
                .build();
        return team;
    }
}