package com.project.awesomegroup.service;

import com.project.awesomegroup.dto.Team;
import com.project.awesomegroup.dto.teammember.TeamMember;
import com.project.awesomegroup.dto.user.User;
import com.project.awesomegroup.repository.TeamRepository;
import com.project.awesomegroup.repository.UserRepository;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    //Create
    @Transactional
    public Team insert(Team team) {
        //유저 조회
        Optional<User> user = userRepository.findById(team.getTeamMaster());
        //팀멤버 생성
        TeamMember teamMember = TeamMember.createTeamMember(user.get(), 2);
        //팀 생성
        Team newTeam = Team.createTeam(team.getTeamName(), team.getTeamPurpose(), team.getTeamInfo(), teamMember);
        teamRepository.save(newTeam);
        return newTeam;
    }

    //Read
    public Team findByTeamId(Long teamId){
        Optional<Team> team = teamRepository.findById(teamId);
        return team.get();
    }

    public List<Team> findAll() {
        return teamRepository.findAll();
    }

    //Update
    @Transactional
    public boolean update(Team team) {
        try {
            teamRepository.save(team);
            return true;
        }catch (PersistenceException e){
            return false;
        }
    }

    //Delete
    @Transactional
    public boolean delete(Long team_id) {
        try{
            teamRepository.deleteById(team_id);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
