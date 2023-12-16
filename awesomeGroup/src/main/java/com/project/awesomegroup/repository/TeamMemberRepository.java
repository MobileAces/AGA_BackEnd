package com.project.awesomegroup.repository;

import com.project.awesomegroup.dto.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    List<TeamMember> findByUser_UserId(String id);
    List<TeamMember> findByTeam_TeamId(Long id);
}
