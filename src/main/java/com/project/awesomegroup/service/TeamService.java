package com.project.awesomegroup.service;

import com.project.awesomegroup.dto.team.Team;
import com.project.awesomegroup.dto.team.request.TeamRegistRequest;
import com.project.awesomegroup.dto.team.request.TeamUpdateRequest;
import com.project.awesomegroup.dto.team.response.TeamResponse;
import com.project.awesomegroup.dto.team.response.TeamUpdateResponse;
import com.project.awesomegroup.dto.team.response.TeamUpdateResponseDTO;
import com.project.awesomegroup.dto.teammember.TeamMember;
import com.project.awesomegroup.dto.user.User;
import com.project.awesomegroup.dto.user.request.UserUpdateRequest;
import com.project.awesomegroup.dto.user.response.UserResponse;
import com.project.awesomegroup.dto.user.response.UserResponseDTO;
import com.project.awesomegroup.repository.TeamRepository;
import com.project.awesomegroup.repository.UserRepository;
import jakarta.persistence.EntityManager;
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
    private final UserService userService;
    private final EntityManager entityManager;

    //Create
    @Transactional
    public TeamResponse insert(TeamRegistRequest request) {
        //유저 조회
        Optional<User> user = userRepository.findById(request.getTeamMaster());
        if(!user.isPresent()) {
            return TeamResponse.createTeamResponse("User Not Found", 404, null);
        }
        //팀멤버 생성
        TeamMember teamMember = TeamMember.createTeamMember(user.get(), 2);
        //팀 생성
        Team newTeam = Team.createTeam(request.getTeamName(), request.getTeamInfo(), teamMember);
        teamRepository.save(newTeam);
        return TeamResponse.createTeamResponse("Team Regist Success", 201, newTeam);
    }

    //Read
    public TeamResponse findByTeamId(Integer teamId){
        Optional<Team> checkTeam =  teamRepository.findById(teamId);
        if(checkTeam.isPresent()){ //해당 id가 존재할 때
            Team team = checkTeam.get();
            return TeamResponse.createTeamResponse("Team Found", 200, team);
        }else{
            //존재 하지 않을 때
            return TeamResponse.createTeamResponse("Team not Found", 404, null);
        }
    }

    public List<Team> findAll() {
        return teamRepository.findAll();
    }

    //Update
    @Transactional
    public TeamUpdateResponse update(TeamUpdateRequest request) {

        Team findTeam = entityManager.find(Team.class, request.getTeamId());
        if(findTeam != null){
            try { //유저 정보 업데이트
                if(request.getTeamName() != null){findTeam.setTeamName(request.getTeamName());}
                if(request.getTeamInfo() != null){findTeam.setTeamInfo(request.getTeamInfo());}
                if(request.getTeamMaster() != null){findTeam.setTeamMaster(request.getTeamMaster());}
                return TeamUpdateResponse.createTeamUpdateResponse("Success", 200, new TeamUpdateResponseDTO(findTeam.getTeamId(), findTeam.getTeamName(), findTeam.getTeamInfo(), findTeam.getTeamMaster()));
            }catch (PersistenceException e){
                return TeamUpdateResponse.createTeamUpdateResponse("Fail", 500, null);
            }
        }else{return TeamUpdateResponse.createTeamUpdateResponse("Team not Found", 404, null);}
    }



    //Delete
    @Transactional
    public boolean delete(Integer team_id) {
        try{
            teamRepository.deleteById(team_id);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
