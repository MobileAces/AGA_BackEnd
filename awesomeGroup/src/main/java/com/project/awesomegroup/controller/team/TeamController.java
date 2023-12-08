package com.project.awesomegroup.controller.team;

import com.project.awesomegroup.dto.Team;
import com.project.awesomegroup.service.TeamService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/teams")
@Tag(name = "Team", description = "Team API")
public class TeamController {
    private static final Logger logger = LoggerFactory.getLogger(TeamController.class);

    @Autowired
    TeamService teamService;

    @GetMapping("/all")
    public List<Team> teamAllSelect(){
        return teamService.findAll();
    }

    @GetMapping("/teams/{team_id}")
    public Team teamSelect(@RequestParam int team_id){
        return teamService.findByTeamId(team_id);
    }

    @PostMapping("/teams")
    public int teamSave(@RequestBody Team team){
        return teamService.insert(team);
    }

    @PutMapping("/teams/{team_id}")
    public int teamUpdate(@RequestBody Team team){
        return teamService.update(team);
    }

    @DeleteMapping("/teams/{team_id}")
    public void teamDelete(@RequestParam int team_id){
        teamService.delete(team_id);
    }

}
