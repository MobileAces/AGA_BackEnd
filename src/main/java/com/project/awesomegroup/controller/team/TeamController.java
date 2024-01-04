package com.project.awesomegroup.controller.team;

import com.project.awesomegroup.dto.team.Team;
import com.project.awesomegroup.dto.team.request.TeamRegistRequest;
import com.project.awesomegroup.dto.team.request.TeamUpdateRequest;
import com.project.awesomegroup.dto.team.response.TeamDeleteResponse;
import com.project.awesomegroup.dto.team.response.TeamResponse;
import com.project.awesomegroup.dto.team.response.TeamUpdateResponse;
import com.project.awesomegroup.dto.user.response.UserResponse;
import com.project.awesomegroup.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    @Operation(summary = "팀 전체 조회", description = "모든 팀의 정보를 반환합니다.")
    @GetMapping("/entirety")
    public List<Team> teamAllSelect(){
        return teamService.findAll();
    }

    @Operation(summary = "팀 조회", description = "teamId에 맞는 팀 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공 (message : \"Team Found\", code : 200)", content = @Content(schema = @Schema(implementation = TeamResponse.class))),
            @ApiResponse(responseCode = "404", description = "조회 실패 (message : \"Team not Found\", code : 404, data : null)", content = @Content)
    })
    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponse> teamSelect(@PathVariable Integer teamId){
        TeamResponse checkTeam = teamService.findByTeamId(teamId);
        if(checkTeam.getCode() == 200){
            //해당하는 ID 정보를 찾았을 때 (code = 200)
            return ResponseEntity.ok(checkTeam);
        }else{
            //정보를 찾지 못했을 때 (code = 404)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(checkTeam);
        }
    }


    @Operation(summary = "팀 등록", description = "팀 정보를 기입해 팀 정보를 저장합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "팀 등록 성공 (message : \"Team Create Success\", code : 201)", content = @Content(schema = @Schema(implementation = TeamResponse.class))),
            @ApiResponse(responseCode = "404", description = "팀 등록 실패 (message : \"User Not Found\", code : 404, data : null)", content = @Content)
    })
    @PostMapping
    public ResponseEntity<TeamResponse> teamInsert(@RequestBody TeamRegistRequest request){
        TeamResponse checkTeam = teamService.insert(request);
        if(checkTeam.getCode() == 404){
            //팀 등록 실패 시 (유저 조회 불가) (code = 404)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(checkTeam);
        }
        //팀 등록 성공 시 (code = 201)
        return ResponseEntity.status(HttpStatus.CREATED).body(checkTeam);
    }

    @Operation(summary = "팀 수정", description = "팀 Name, Info, Master 입력된 것만 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공 (message : \"Success\", code : 200)", content = @Content(schema = @Schema(implementation = TeamUpdateResponse.class))),
            @ApiResponse(responseCode = "404", description = "수정 실패 (message : \"Team not Found\", code : 404, data : null)", content = @Content),
            @ApiResponse(responseCode = "500", description = "수정 실패 (message : \"Fail\", code : 500, data : null)", content = @Content)
    })
    @PutMapping
    public TeamUpdateResponse teamUpdate(@RequestBody TeamUpdateRequest request){
        return teamService.update(request);
    }

    @Operation(summary = "팀 삭제", description = "팀 ID를 입력 받아 팀 정보를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공 (message : \"Success\", code : 200, data : true)", content = @Content(schema = @Schema(implementation = TeamDeleteResponse.class))),
            @ApiResponse(responseCode = "404", description = "삭제 실패 (message : \"Team not Found\", code : 404, data : false)", content = @Content),
            @ApiResponse(responseCode = "500", description = "삭제 실패 (message : \"Server Error\", code : 500, data : true)", content = @Content)
    })
    @DeleteMapping("/{teamId}")
    public TeamDeleteResponse teamDelete(@PathVariable Integer teamId){
        return teamService.delete(teamId);
    }
}
