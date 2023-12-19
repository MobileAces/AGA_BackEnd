package com.project.awesomegroup.service;

import com.project.awesomegroup.dto.teammember.TeamMember;
import com.project.awesomegroup.dto.teammember.TeamMemberResponse;
import com.project.awesomegroup.repository.TeamMemberRepository;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;

    @Transactional
    public TeamMember regist(Integer teamId, String userId) {
        TeamMember findTeamMember = teamMemberRepository.findByTeamTeamIdAndUserUserId(teamId, userId);
        if(findTeamMember == null) {
            TeamMember newTeamMember = TeamMember.createTeamMember(teamId, userId, 0);
            findTeamMember = teamMemberRepository.save(newTeamMember);
        }
        return findTeamMember;
    }

    public List<TeamMember> getTeamMembersByUserId(String id) {
        return teamMemberRepository.findByUser_UserId(id);
    }

    public List<TeamMember> getTeamMembersByTeamId(Long id) {
        return teamMemberRepository.findByTeam_TeamId(id);
    }

    @Transactional
    public boolean update(TeamMemberResponse teamMemberResponse) {
        try {
            TeamMember teamMember = teamMemberRepository.findByTeamTeamIdAndUserUserId(teamMemberResponse.getTeamId(), teamMemberResponse.getUserId());
            teamMember.setAuthority(teamMemberResponse.getAuthority());
            return true;
        }catch (PersistenceException e){
            return false;
        }
    }

//    @Transactional
//    public boolean delete(Integer teamId, String userId) {
//        try{
//            TeamMember findMember = teamMemberRepository.findByTeamTeamIdAndUserUserId(teamId, userId);
//            teamMemberRepository.delete(findMember);
//            return true;
//        } catch (Exception e){
//            return false;
//        }
//    }

}
