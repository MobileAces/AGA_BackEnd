package com.project.awesomegroup.repository;

import com.project.awesomegroup.dto.User;
import com.project.awesomegroup.dto.teammember.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamMemberRepository extends JpaRepository<TeamMember, String> {
    List<TeamMember> findByUserUserId(String id);
}
