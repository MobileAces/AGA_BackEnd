package com.project.awesomegroup.repository;

import com.project.awesomegroup.dto.teammember.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Integer> {

    List<TeamMember> findByUser_UserId(String id);
    List<TeamMember> findByTeam_TeamId(Integer id);
    Optional<TeamMember> findByTeamTeamIdAndUserUserId(Integer teamId, String userId);

}
