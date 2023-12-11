package com.project.awesomegroup.service;

import com.project.awesomegroup.dto.Team;
import com.project.awesomegroup.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {
    @Autowired
    private TeamRepository teamRepository;

    //Create
    public int insert(Team team) {
        if(teamRepository.findById(team.getTeamId()).isPresent()){
            return -1;
        } else {
            Team savedTeam = teamRepository.save(team);
            return savedTeam.getTeamId();
        }
    }

    //Read
    public Team findByTeamId(int teamId){
        Optional<Team> optionalTeam = teamRepository.findById(teamId);
        return optionalTeam.get();
    }

    public List<Team> findAll() {
        return teamRepository.findAll();
    }

    //Update
    public int update(Team team) {
        if(teamRepository.findById(team.getTeamId()).isPresent()){
            Team savedTeam = teamRepository.save(team);
            return savedTeam.getTeamId();
        } else {
            return -1;
        }
    }

    //Delete
    public void delete(int team_id) {
        Optional<Team> optionalTeam = teamRepository.findById(team_id);
        teamRepository.delete(optionalTeam.get());
    }
}
