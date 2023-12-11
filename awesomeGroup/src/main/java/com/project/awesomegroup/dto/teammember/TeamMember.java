package com.project.awesomegroup.dto.teammember;

import com.project.awesomegroup.dto.Team;
import com.project.awesomegroup.dto.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "teammember")
@IdClass(TeamMemberId.class)
public class TeamMember {
    @Id
    @ManyToOne
    @JoinColumn(name="team_id", nullable = false)
    private Team team;

    @Id
    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String authority;

}
