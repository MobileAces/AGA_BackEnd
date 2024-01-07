package com.project.awesomegroup.dto.invate;

import com.project.awesomegroup.dto.team.Team;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "invite")
public class Invitation {
    @Id @Column(name = "invite_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String inviteCode;

    @Column
    private LocalDateTime expirationDateTime;

    @OneToOne
    @JoinColumn(name = "team_id")
    private Team team;

    public static Invitation createInvitation(String inviteCode, LocalDateTime expirationDateTime, Team team){
        return Invitation.builder()
                .inviteCode(inviteCode)
                .expirationDateTime(expirationDateTime)
                .team(team)
                .build();
    }

}
