package com.project.awesomegroup.repository;

import com.project.awesomegroup.dto.invate.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InviteRespository extends JpaRepository<Invitation, Integer> {
    Optional<Invitation> findByInviteCode(String inviteCode);

    Optional<Invitation> findByTeam_TeamId(Integer teamId);
}
