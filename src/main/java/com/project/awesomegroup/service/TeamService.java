package com.project.awesomegroup.service;

import com.project.awesomegroup.dto.team.Team;
import com.project.awesomegroup.dto.team.request.TeamRegistRequest;
import com.project.awesomegroup.dto.team.request.TeamUpdateRequest;
import com.project.awesomegroup.dto.team.response.*;
import com.project.awesomegroup.dto.teammember.TeamMember;
import com.project.awesomegroup.dto.user.User;
import com.project.awesomegroup.dto.user.request.UserUpdateRequest;
import com.project.awesomegroup.dto.user.response.UserResponse;
import com.project.awesomegroup.dto.user.response.UserResponseDTO;
import com.project.awesomegroup.repository.TeamRepository;
import com.project.awesomegroup.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamService {
    private static final Logger logger = LoggerFactory.getLogger(TeamService.class);

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    //Create
    @Transactional
    public ResponseEntity<TeamResponse> insert(TeamRegistRequest request) {
        //유저 조회
        Optional<User> user = userRepository.findById(request.getTeamMaster());
        if(user.isEmpty()) {
            TeamResponse response = TeamResponse.createTeamResponse("User Not Found", 404, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        //팀멤버 생성
        TeamMember teamMember = TeamMember.createTeamMember(user.get(), 2);
        //팀 생성
        Team newTeam = Team.createTeam(request.getTeamName(), request.getTeamInfo(), teamMember);
        teamRepository.save(newTeam);
        TeamResponse response = TeamResponse.createTeamResponse("Team Regist Success", 201, newTeam);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //Read
    public ResponseEntity<TeamResponse> findByTeamId(Integer teamId){
        Optional<Team> checkTeam =  teamRepository.findById(teamId);
        if(checkTeam.isPresent()){ //해당 id가 존재할 때
            Team team = checkTeam.get();
            TeamResponse response = TeamResponse.createTeamResponse("Team Found", 200, team);
            return ResponseEntity.ok(response);
        }else{
            //존재 하지 않을 때
            TeamResponse response = TeamResponse.createTeamResponse("Team not Found", 404, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    public List<Team> findAll() {
        return teamRepository.findAll();
    }

    //Update
    @Transactional
    public ResponseEntity<TeamUpdateResponse> update(TeamUpdateRequest request) {
        Optional<Team> checkTeam = teamRepository.findById(request.getTeamId());
        if(checkTeam.isPresent()){
            String teamName = checkTeam.get().getTeamName();
            String teamInfo = checkTeam.get().getTeamInfo();
            String teamMaster = checkTeam.get().getTeamMaster();
            try{
                if(request.getTeamName() != null){
                    teamName = request.getTeamName();
                    Query query = entityManager.createQuery("UPDATE Team u SET u.teamName = :newName WHERE u.teamId = :teamId");
                    query.setParameter("newName", request.getTeamName());
                    query.setParameter("teamId", request.getTeamId());
                    query.executeUpdate();
                }
                if(request.getTeamInfo() != null){
                    teamInfo = request.getTeamInfo();
                    Query query = entityManager.createQuery("UPDATE Team u SET u.teamInfo = :newInfo WHERE u.teamId = :teamId");
                    query.setParameter("newInfo", request.getTeamInfo());
                    query.setParameter("teamId", request.getTeamId());
                    query.executeUpdate();
                }
                if(request.getTeamName() != null){
                    teamMaster = request.getTeamMaster();
                    Query query = entityManager.createQuery("UPDATE Team u SET u.teamMaster = :newMaster WHERE u.teamId = :teamId");
                    query.setParameter("newMaster", request.getTeamMaster());
                    query.setParameter("teamId", request.getTeamId());
                    query.executeUpdate();
                }
                TeamUpdateResponse response = TeamUpdateResponse.createTeamUpdateResponse("Success", 200, TeamUpdateResponseDTO.createTeamUpdateResponse(checkTeam.get(), teamName, teamInfo, teamMaster));
                return ResponseEntity.ok(response);
            }catch (PersistenceException e){
                TeamUpdateResponse response = TeamUpdateResponse.createTeamUpdateResponse("Fail", 500, null);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }
        TeamUpdateResponse response = TeamUpdateResponse.createTeamUpdateResponse("Team not Found", 404, null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    //Delete
    @Transactional
    public ResponseEntity<TeamDeleteResponse> delete(Integer teamId) {
        Optional<Team> findTeam = teamRepository.findById(teamId);
        if(findTeam.isEmpty()) {
            //팀을 찾지 못했을 때 (code = 404)
            TeamDeleteResponse response = TeamDeleteResponse.createTeamDeleteResponse("Team not Found", 404, TeamDeleteResponseDTO.createTeamDeleteResponseDTO(false));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        try{
            teamRepository.deleteById(teamId);
            //삭제를 정상적으로 실행 했을 때 (code = 200)
            TeamDeleteResponse response = TeamDeleteResponse.createTeamDeleteResponse("Success", 200, TeamDeleteResponseDTO.createTeamDeleteResponseDTO(true));
            return ResponseEntity.ok(response);
        } catch (Exception e){
            //service 단에서 에러가 발생한 경우 (code = 500)
            TeamDeleteResponse response = TeamDeleteResponse.createTeamDeleteResponse("Server Error", 500, TeamDeleteResponseDTO.createTeamDeleteResponseDTO(false));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
