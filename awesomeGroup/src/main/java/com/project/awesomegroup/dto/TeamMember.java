package com.project.awesomegroup.dto;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@Table(name = "teammember")
public class TeamMember {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teammember_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "authority")
    private int authority;

    //==생성 메서드==//
    public static TeamMember createTeamMember(User user, int authority) {
        TeamMember teamMember = new TeamMember();
        teamMember.setUser(user);
        teamMember.setAuthority(authority);

        return teamMember;
    }

}
