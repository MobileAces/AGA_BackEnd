package com.project.awesomegroup.service;

import com.project.awesomegroup.dto.TeamMember;
import com.project.awesomegroup.repository.TeamMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamMemberService {
    @Autowired
    TeamMemberRepository teamMemberRepository;

    public List<TeamMember> getTeamMembersByUserId(String id) {
        return teamMemberRepository.findByUser_UserId(id);
    }

    public List<TeamMember> getTeamMembersByTeamId(Long id) {
        return teamMemberRepository.findByTeam_TeamId(id);
    }

}
