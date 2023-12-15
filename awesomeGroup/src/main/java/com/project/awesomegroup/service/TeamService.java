package com.project.awesomegroup.service;

import com.project.awesomegroup.dto.Team;
import com.project.awesomegroup.dto.User;
import com.project.awesomegroup.repository.TeamRepository;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {
    @Autowired
    private TeamRepository teamRepository;

    //Create
    public Team insert(Team team) {
        Team savedTeam = teamRepository.save(team);
        return savedTeam;
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
    public boolean update(Team team) {
        try {
            teamRepository.save(team);
            return true;
        }catch (PersistenceException e){
            return false;
        }
    }

    //Delete
    public boolean delete(Long team_id) {
        try{
            teamRepository.deleteById(team_id);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
