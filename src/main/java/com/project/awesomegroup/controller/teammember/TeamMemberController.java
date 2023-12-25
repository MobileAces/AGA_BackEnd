package com.project.awesomegroup.controller.teammember;

import com.project.awesomegroup.dto.teammember.request.TeamMemberRequest;
import com.project.awesomegroup.dto.teammember.response.TeamMemberResponse;
import com.project.awesomegroup.dto.teammember.response.TeamMemberResponseDTO;
import com.project.awesomegroup.dto.teammember.response.team.TeamMemberTeamListResponse;
import com.project.awesomegroup.dto.teammember.response.user.TeamMemberUserListResponse;
import com.project.awesomegroup.service.TeamMemberService;
import com.project.awesomegroup.service.TeamService;
import com.project.awesomegroup.service.UserService;
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

import java.util.Map;


@RestController
@CrossOrigin("*")
@RequestMapping("/members")
@Tag(name="TeamMember", description = "TeamMember API")
public class TeamMemberController {
    private static final Logger logger = LoggerFactory.getLogger(TeamMemberController.class);

    @Autowired
    TeamMemberService teamMemberService;

    @Autowired
    TeamService teamService;

    @Autowired
    UserService userService;

    @Operation(summary = "팀 멤버 등록", description = "유저 아이디, 팀 아이디를 기입해 팀멤버 정보를 저장합니다.(기본 권한으로 주어짐)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "팀 멤버 등록 성공 (message : \"Success\", code : 200)", content = @Content(schema = @Schema(implementation = TeamMemberResponse.class))),
            @ApiResponse(responseCode = "400", description = "팀 멤버 등록 실패 (message : \"Failed\", code : 400, data : null)", content = @Content)
    })
    @PostMapping
    public ResponseEntity<TeamMemberResponse> regist(@RequestParam Integer teamId, @RequestParam String userId){

        TeamMemberResponse checkTeamMember = teamMemberService.regist(teamId, userId);
        if(checkTeamMember.getCode() == 400){
            //팀 멤버 등록 실패 시 (사용자 이미 존재함) (code = 400)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(checkTeamMember);
        }
        //팀 멤버 등록 성공 시 (code = 200)
        return ResponseEntity.ok(checkTeamMember);
    }

    @Operation(summary = "유저가 속한 팀 조회", description = "userId에 맞는 팀들을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공 (message : \"Success\", code : 200)", content = @Content(schema = @Schema(implementation = TeamMemberTeamListResponse.class))),
            @ApiResponse(responseCode = "404", description = "조회 실패 (message : \"Not Found\", code : 404, data : null)", content = @Content)
    })
    @GetMapping("/teams")
    public ResponseEntity<TeamMemberTeamListResponse> selectByUserId(@RequestParam String id) {
        TeamMemberTeamListResponse response = teamMemberService.getTeamMembersByUserId(id);
        if(response.getCode() == 404) {
            //userId 에 해당하는 팀이 없을 때 (code = 404)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        //userId 에 해당하는 팀이 존재할 때 (code = 200)
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "팀에 속한 유저 조회", description = "teamId에 맞는 유저들을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공 (message : \"Success\", code : 200)", content = @Content(schema = @Schema(implementation = TeamMemberUserListResponse.class))),
            @ApiResponse(responseCode = "404", description = "조회 실패 (message : \"Not Found\", code : 404, data : null)", content = @Content)
    })
    @GetMapping("/users")
    public ResponseEntity<TeamMemberUserListResponse> selectByTeamId(@RequestParam Integer id) {

        TeamMemberUserListResponse response = teamMemberService.getTeamMembersByTeamId(id);
        if(response.getCode() == 404) {
            //teamId 에 해당하는 유저가 없을 때 (code = 404)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        //teamId 에 해당하는 유저가 존재할 때 (code = 200)
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "팀멤버 수정", description = "특정 팀에 대한 해당 유저의 권한을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공 (message : \"Success\", code : 200)", content = @Content(schema = @Schema(implementation = TeamMemberResponse.class))),
            @ApiResponse(responseCode = "404", description = "수정 실패 (message : \"User not Found\", code : 404, data : null)", content = @Content),
            @ApiResponse(responseCode = "404", description = "수정 실패 (message : \"Team not Found\", code : 404, data : null)", content = @Content),
            @ApiResponse(responseCode = "404", description = "수정 실패 (message : \"User does not exist in the team.\", code : 404, data : null)", content = @Content),
    })
    @PutMapping
    public ResponseEntity<TeamMemberResponse> update(@RequestBody TeamMemberRequest request){
        if(teamService.findByTeamId(request.getTeamId()).getCode() == 404)
            //팀이 존재하지 않을 때 (code = 404)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(TeamMemberResponse.createTeamMemberResponseDTO("Team not Found", 404, null));
        if(userService.select(request.getUserId()).getCode() == 404)
            //유저가 존재하지 않을 때 (code = 404)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(TeamMemberResponse.createTeamMemberResponseDTO("User not Found", 404, null));

        TeamMemberResponse response = teamMemberService.update(request);
        if (response.getCode() == 404) {
            //유저가 팀에 속해 있지 않을 때 (code = 404)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        //성공적으로 수정이 되었을 때 (code = 200)
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "팀 멤버 삭제", description = "팀 아이디와, 유저 아이디로 팀 멤버 정보를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공 (string : \"Success\")", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "삭제 실패 (string : \"User not Found\")", content = @Content),
            @ApiResponse(responseCode = "404", description = "삭제 실패 (string : \"Team not Found\")", content = @Content),
            @ApiResponse(responseCode = "404", description = "삭제 실패 (string : \"User not Found\")", content = @Content),
    })
    @DeleteMapping("/{teamId}/{userId}")
    public ResponseEntity<String> delete(@PathVariable Integer teamId, @PathVariable String userId){
        if(teamService.findByTeamId(teamId).getCode() == 404)
            //팀이 존재하지 않을 때 (code = 404)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team not Found");
        if(userService.select(userId).getCode() == 404)
            //유저가 존재하지 않을 때 (code = 404)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not Found");

        Map<String, String> map = teamMemberService.delete(teamId, userId);
        String response = map.get("result");
        if(response.equals("Success")) {
            //성공적으로 삭제가 되었을 때 (code = 200)
            return ResponseEntity.ok(response);
        }
        //팀에 속한 유저가 존재하지 않을 때 (code = 404)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

}
