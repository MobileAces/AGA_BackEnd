package com.project.awesomegroup.controller.teammember;

import com.project.awesomegroup.dto.teammember.request.TeamMemberRequest;
import com.project.awesomegroup.dto.teammember.response.TeamMemberDeleteResponse;
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
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class TeamMemberController {
    private static final Logger logger = LoggerFactory.getLogger(TeamMemberController.class);

    private final TeamMemberService teamMemberService;

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
            @ApiResponse(responseCode = "404", description = "수정 실패 (message : \"User not Found\", code : 404, data : null)\n" +
                    "\n" +
                    "수정 실패 (message : \"Team not Found\", code : 404, data : null)\n" +
                    "\n" +
                    "수정 실패 (message : \"User does not exist in the team.\", code : 404, data : null)", content = @Content)
    })
    @PutMapping
    public ResponseEntity<TeamMemberResponse> update(@RequestBody TeamMemberRequest request){
        return teamMemberService.update(request);
    }

    @Operation(summary = "팀 멤버 삭제", description = "팀 아이디와, 유저 아이디로 팀 멤버 정보를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공 (message : \"Success\", code : 200, data : true)", content = @Content(schema = @Schema(implementation = TeamMemberDeleteResponse.class))),
            @ApiResponse(responseCode = "404", description = "삭제 실패 (message : \"User not Found\", code : 404, data : false)\n" +
                    "\n" +
                    "삭제 실패 (message : \"Team not Found\", code : 404, data : false)\n" +
                    "\n" +
                    "삭제 실패 (message : \"User does not exist in the team.\", code : 404, data : false)", content = @Content),
    })
    @DeleteMapping("/{teamId}/{userId}")
    public TeamMemberDeleteResponse delete(@PathVariable Integer teamId, @PathVariable String userId){
        return teamMemberService.delete(teamId, userId);
    }

}
