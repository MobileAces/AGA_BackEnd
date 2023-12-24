package com.project.awesomegroup.dto.teammember;

import com.project.awesomegroup.dto.Team;
import com.project.awesomegroup.dto.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
        TeamMember teamMember = TeamMember.builder()
                .user(user)
                .authority(authority)
                .build();
        return teamMember;
    }

    public static TeamMember createTeamMember(Integer teamId, String userId, int authority) {
        TeamMember teamMember = TeamMember.builder()
                .team(Team.createTeam(teamId))
                .user(new User(userId))
                .authority(authority)
                .build();
        return teamMember;
    }

}
