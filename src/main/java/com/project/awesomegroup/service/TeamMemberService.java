package com.project.awesomegroup.service;

import com.project.awesomegroup.controller.teammember.TeamMemberController;
import com.project.awesomegroup.dto.team.Team;
import com.project.awesomegroup.dto.teammember.TeamMember;
import com.project.awesomegroup.dto.teammember.request.TeamMemberRequest;
import com.project.awesomegroup.dto.teammember.response.*;
import com.project.awesomegroup.dto.teammember.response.team.TeamMemberInfo;
import com.project.awesomegroup.dto.teammember.response.team.TeamMemberTeamListResponse;
import com.project.awesomegroup.dto.teammember.response.team.TeamMemberUserList;
import com.project.awesomegroup.dto.teammember.response.user.TeamMemberUserListResponse;
import com.project.awesomegroup.dto.teammember.response.user.TeamMemberUserResponseDTO;
import com.project.awesomegroup.dto.user.User;
import com.project.awesomegroup.repository.TeamMemberRepository;
import com.project.awesomegroup.repository.TeamRepository;
import com.project.awesomegroup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamMemberService {
    private static final Logger logger = LoggerFactory.getLogger(TeamMemberController.class);

    private final TeamMemberRepository teamMemberRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity<TeamMemberResponse> regist(Integer teamId, String userId) {
        //유저가 팀에 등록되어있는지 확인
        Optional<TeamMember> findTeamMember = teamMemberRepository.findByTeamTeamIdAndUserUserId(teamId, userId);
        if(findTeamMember.isPresent()) {
            //유저가 팀에 등록되어 있다면 Failed 반환 (code = 400)
            TeamMemberResponse response = TeamMemberResponse.createTeamMemberResponseDTO("Failed", 400, null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        //팀 등록 진행 (code = 200)
        TeamMember newTeamMember = TeamMember.createTeamMember(teamId, userId, 0);
        teamMemberRepository.save(newTeamMember);
        TeamMemberResponse response = TeamMemberResponse.createTeamMemberResponseDTO("Success", 200, TeamMemberResponseDTO.createTeamMemberResponse(newTeamMember));
        return ResponseEntity.ok(response);
    }
    public ResponseEntity<TeamMemberTeamListResponse> getTeamMembersByUserId(String id) {
        //유저가 속한 팀 조회 (복수)
        List<TeamMember> list = teamMemberRepository.findByUser_UserId(id);
        List<TeamMemberInfo> teamMemberInfos = new ArrayList<>();
        //각 팀에 대한 유저를 조회해서 팀 정보로 list에 저장한다.
        list.forEach(e->
                //각 팀 정보를 List로 저장한다.
                teamMemberInfos.add(
                        //팀 정보를 생성한다.
                        TeamMemberInfo.teamMemberInfoCreate(
                                //각 팀에 속한 유저 조회 (복수) : 유저리스트 저장함 (반환값이 List임)
                                teamMemberRepository.findByTeam_TeamId(e.getTeam().getTeamId())
                        )
                )
                );
        if(teamMemberInfos.isEmpty()) {
            TeamMemberTeamListResponse response = TeamMemberTeamListResponse.createTeamMemberUserListResponse("Not Found", 404, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        TeamMemberTeamListResponse response = TeamMemberTeamListResponse.createTeamMemberUserListResponse("Success", 200, teamMemberInfos);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<TeamMemberUserListResponse> getTeamMembersByTeamId(Integer id) {
        List<TeamMemberUserResponseDTO> findList = new ArrayList<>();
        teamMemberRepository.findByTeam_TeamId(id).forEach(e -> {
            findList.add(TeamMemberUserResponseDTO.builder()
                            .userId(e.getUser().getUserId())
                            .userNickname(e.getUser().getUserNickname())
                            .authority(e.getAuthority())
                            .build());
        });
        if(findList.isEmpty()) {
            TeamMemberUserListResponse response = TeamMemberUserListResponse.createTeamMemberUserListResponse("Not Found", 404, id, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        TeamMemberUserListResponse response = TeamMemberUserListResponse.createTeamMemberUserListResponse("Success", 200, id, findList);
        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<TeamMemberResponse> update(TeamMemberRequest request) {

        Optional<Team> findTeam = teamRepository.findById(request.getTeamId());
        if(findTeam.isEmpty()) {
            //팀이 존재하지 않을 때 (code = 404)
            TeamMemberResponse response = TeamMemberResponse.createTeamMemberResponseDTO("Team not Found", 404, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Optional<User> findUser = userRepository.findByUserId(request.getUserId());
        if(findUser.isEmpty()) {
            //유저가 존재하지 않을 때 (code = 404)
            TeamMemberResponse response = TeamMemberResponse.createTeamMemberResponseDTO("User not Found", 404, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Optional<TeamMember> teamMember = teamMemberRepository.findByTeamTeamIdAndUserUserId(request.getTeamId(), request.getUserId());
        if(teamMember.isEmpty()) {
            //팀멤버로 등록된 정보가 존재하지 않을 때 (code = 404)
            TeamMemberResponse response = TeamMemberResponse.createTeamMemberResponseDTO("User does not exist in the team.", 404, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        //팀멤버로 수정되었을 때 (code = 200)
        teamMember.get().setAuthority(request.getAuthority());
        TeamMemberResponse response = TeamMemberResponse.createTeamMemberResponseDTO("Success", 200, TeamMemberResponseDTO.createTeamMemberResponse(teamMember.get()));
        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<TeamMemberDeleteResponse> delete(Integer teamId, String userId) {
        TeamMemberDeleteResponse response;
        //팀 조회
        Optional<Team> findTeam = teamRepository.findById(teamId);
        if(findTeam.isEmpty()){
            response = TeamMemberDeleteResponse.teamMemberDeleteResponseCreate("Team not Found", 404, TeamMemberDeleteResponseDTO.teamMemberDeleteResponseDTOCreate(false));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        //유저 조회
        Optional<User> findUser = userRepository.findByUserId(userId);
        if(findUser.isEmpty()){
            response = TeamMemberDeleteResponse.teamMemberDeleteResponseCreate("User not Found", 404, TeamMemberDeleteResponseDTO.teamMemberDeleteResponseDTOCreate(false));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        //팀 멤버 조회
        Optional<TeamMember> findMember = teamMemberRepository.findByTeamTeamIdAndUserUserId(teamId, userId);
        if (findMember.isEmpty()) {
            //팀멤버로 등록된 정보가 존재하지 않을 때 (code = 404)
            response = TeamMemberDeleteResponse.teamMemberDeleteResponseCreate("User does not exist in the team", 404, TeamMemberDeleteResponseDTO.teamMemberDeleteResponseDTOCreate(false));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        //팀 멤버 삭제 완료(code = 200)
        teamMemberRepository.delete(findMember.get());
        response = TeamMemberDeleteResponse.teamMemberDeleteResponseCreate("Success", 200, TeamMemberDeleteResponseDTO.teamMemberDeleteResponseDTOCreate(true));
        return ResponseEntity.ok(response);
    }
}
