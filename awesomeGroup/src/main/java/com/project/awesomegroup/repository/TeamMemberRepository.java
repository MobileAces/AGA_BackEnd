package com.project.awesomegroup.repository;

import com.project.awesomegroup.dto.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    List<TeamMember> findByUser_UserId(String id);
    List<TeamMember> findByTeam_TeamId(Long id);
}
