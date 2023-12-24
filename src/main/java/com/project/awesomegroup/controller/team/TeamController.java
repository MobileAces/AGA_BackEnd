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

    @GetMapping("/entirety")
    public List<Team> teamAllSelect(){
        return teamService.findAll();
    }

    @GetMapping("/{team_id}")
    public Team teamSelect(@PathVariable Long team_id){
        return teamService.findByTeamId(team_id);
    }

    @PostMapping
    public Team teamInsert(@RequestBody Team team){ return teamService.insert(team); }

    @PutMapping
    public boolean teamUpdate(@RequestBody Team team){
        return teamService.update(team);
    }

    @DeleteMapping("/{team_id}")
    public boolean teamDelete(@PathVariable Long team_id){
        return teamService.delete(team_id);
    }
}
