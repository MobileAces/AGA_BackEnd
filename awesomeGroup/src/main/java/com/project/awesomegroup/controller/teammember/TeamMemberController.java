package com.project.awesomegroup.controller.teammember;

import com.project.awesomegroup.controller.user.UserController;
import com.project.awesomegroup.dto.User;
import com.project.awesomegroup.dto.teammember.TeamMember;
import com.project.awesomegroup.service.TeamMemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin("*")
@RequestMapping("/members")
@Tag(name="TeamMember", description = "TeamMember API")
public class TeamMemberController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    TeamMemberService teamMemberService;

    @GetMapping("/team")
    public List<TeamMember> userInTeam(@RequestParam String id){
        return teamMemberService.userTeam(id);
    }

}
