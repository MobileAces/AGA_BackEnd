package com.project.awesomegroup.service;

import com.project.awesomegroup.controller.teammember.TeamMemberController;
import com.project.awesomegroup.dto.teammember.TeamMember;
import com.project.awesomegroup.dto.teammember.request.TeamMemberRequest;
import com.project.awesomegroup.dto.teammember.response.*;
import com.project.awesomegroup.dto.teammember.response.team.TeamMemberInfo;
import com.project.awesomegroup.dto.teammember.response.team.TeamMemberTeamListResponse;
import com.project.awesomegroup.dto.teammember.response.team.TeamMemberUserList;
import com.project.awesomegroup.dto.teammember.response.user.TeamMemberUserListResponse;
import com.project.awesomegroup.dto.teammember.response.user.TeamMemberUserResponseDTO;
import com.project.awesomegroup.repository.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamMemberService {
    private static final Logger logger = LoggerFactory.getLogger(TeamMemberController.class);

    private final TeamMemberRepository teamMemberRepository;

    @Transactional
    public TeamMemberResponse regist(Integer teamId, String userId) {
        //유저가 팀에 등록되어있는지 확인
        Optional<TeamMember> findTeamMember = teamMemberRepository.findByTeamTeamIdAndUserUserId(teamId, userId);
        if(findTeamMember.isPresent()) {
            //유저가 팀에 등록되어 있다면 Failed 반환 (code = 400)
            return TeamMemberResponse.createTeamMemberResponseDTO("Failed", 400, null);
        }
        //팀 등록 진행 (code = 200)
        TeamMember newTeamMember = TeamMember.createTeamMember(teamId, userId, 0);
        teamMemberRepository.save(newTeamMember);
        return TeamMemberResponse.createTeamMemberResponseDTO("Success", 200, TeamMemberResponseDTO.createTeamMemberResponse(newTeamMember));
    }
    public TeamMemberTeamListResponse getTeamMembersByUserId(String id) {
        //유저가 속한 팀 조회 (복수)
        List<TeamMember> list = teamMemberRepository.findByUser_UserId(id);
        List<TeamMemberInfo> teamMemberInfos = new ArrayList<>();
        //각 팀에 대한 유저를 조회해서 팀 정보로 list에 저장한다.
        list.forEach(e->
                //각 팀 정보를 List로 저장한다.
                teamMemberInfos.add(
                        //팀 정보를 생성한다.
                        TeamMemberInfo.teamMemberInfoCreate(
                                //각 팀에 속한 유저 조회 (복수)
                                teamMemberRepository.findByTeam_TeamId(e.getTeam().getTeamId()
                                )
                        )
                )
                );
        if(teamMemberInfos.isEmpty()) {
            return TeamMemberTeamListResponse.createTeamMemberUserListResponse("Not Found", 404, null);
        }
        return TeamMemberTeamListResponse.createTeamMemberUserListResponse("Success", 200, teamMemberInfos);

    }

    public TeamMemberUserListResponse getTeamMembersByTeamId(Integer id) {
        List<TeamMemberUserResponseDTO> findList = new ArrayList<>();
        teamMemberRepository.findByTeam_TeamId(id).forEach(e -> {
            findList.add(TeamMemberUserResponseDTO.builder()
                            .userId(e.getUser().getUserId())
                            .authority(e.getAuthority())
                            .build());
        });
        if(findList.isEmpty()) {
            return TeamMemberUserListResponse.createTeamMemberUserListResponse("Not Found", 404, id, null);
        }
        return TeamMemberUserListResponse.createTeamMemberUserListResponse("Success", 200, id, findList);
    }

    @Transactional
    public TeamMemberResponse update(TeamMemberRequest request) {
        Optional<TeamMember> teamMember = teamMemberRepository.findByTeamTeamIdAndUserUserId(request.getTeamId(), request.getUserId());
        if(!teamMember.isPresent()) {
            //팀멤버로 등록된 정보가 존재하지 않을 때 (code = 404)
            return TeamMemberResponse.createTeamMemberResponseDTO("User does not exist in the team.", 404, null);
        }
        //팀멤버로 등록되었을 때 (code = 200)
        teamMember.get().setAuthority(request.getAuthority());
        return TeamMemberResponse.createTeamMemberResponseDTO("Success", 200, TeamMemberResponseDTO.createTeamMemberResponse(teamMember.get()));
    }

    @Transactional
    public Map<String, String> delete(Integer teamId, String userId) {
        Map<String, String> map = new HashMap<>();
        Optional<TeamMember> findMember = teamMemberRepository.findByTeamTeamIdAndUserUserId(teamId, userId);
        if (!findMember.isPresent()) {
            //팀멤버로 등록된 정보가 존재하지 않을 때 (code = 404)
            map.put("result", "User does not exist in the team.");
            return map;
        }
        //팀멤버로 등록되었을 때 (code = 200)
        teamMemberRepository.delete(findMember.get());
        map.put("result", "Success");
        return map;
    }

}
