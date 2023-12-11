package com.project.awesomegroup.service;

import com.project.awesomegroup.dto.User;
import com.project.awesomegroup.dto.teammember.TeamMember;
import com.project.awesomegroup.repository.TeamMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamMemberService {
    @Autowired
    TeamMemberRepository teamMemberRepository;

    public List<TeamMember> userTeam(String id){
        return teamMemberRepository.findByUserUserId(id);
    }

}
