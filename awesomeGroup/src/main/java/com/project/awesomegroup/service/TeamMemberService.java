package com.project.awesomegroup.service;

import com.project.awesomegroup.dto.TeamMember;
import com.project.awesomegroup.repository.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;

    @Transactional
    public TeamMember regist(TeamMember teamMember) { return teamMemberRepository.save(teamMember); }

    public List<TeamMember> getTeamMembersByUserId(String id) {
        return teamMemberRepository.findByUser_UserId(id);
    }

    public List<TeamMember> getTeamMembersByTeamId(Long id) {
        return teamMemberRepository.findByTeam_TeamId(id);
    }

}
