package com.project.awesomegroup.controller.teammember;

import com.project.awesomegroup.dto.alarm.Alarm;
import com.project.awesomegroup.dto.teammember.TeamMember;
import com.project.awesomegroup.dto.teammember.TeamMemberResponse;
import com.project.awesomegroup.service.TeamMemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@CrossOrigin("*")
@RequestMapping("/members")
@Tag(name="TeamMember", description = "TeamMember API")
public class TeamMemberController {
    private static final Logger logger = LoggerFactory.getLogger(TeamMemberController.class);

    @Autowired
    TeamMemberService teamMemberService;

    @PostMapping
    public TeamMemberResponse regist(@RequestParam Integer teamId, @RequestParam String userId){
        TeamMember registTeamMember = teamMemberService.regist(teamId, userId);
        TeamMemberResponse response = TeamMemberResponse.createTeamMemberResponse(registTeamMember);
        return response;
    }

    @GetMapping("/teams")
    public List<TeamMemberResponse> selectByUserId(@RequestParam String id) {
        List<TeamMemberResponse> responseDTOList = teamMemberService.getTeamMembersByUserId(id).stream()
                .map(TeamMemberResponse::createTeamMemberResponse)
                .collect(Collectors.toList());
        return responseDTOList;
    }

    @GetMapping("/users")
    public List<TeamMemberResponse> selectByTeamId(@RequestParam Long id) {
        List<TeamMemberResponse> responseDTOList = teamMemberService.getTeamMembersByTeamId(id).stream()
                .map(TeamMemberResponse::createTeamMemberResponse)
                .collect(Collectors.toList());
        return responseDTOList;
    }

    @PutMapping
    public boolean update(@RequestBody TeamMemberResponse teamMemberResponse){
        return teamMemberService.update(teamMemberResponse);
    }

//    @DeleteMapping("/{teamId}/{userId}")
//    public boolean delete(@PathVariable Integer teamId, @PathVariable String userId){
//        return teamMemberService.delete(teamId, userId);
//    }

}
