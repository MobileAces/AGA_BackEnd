package com.project.awesomegroup.controller.alarm;

import com.project.awesomegroup.dto.alarm.Alarm;
import com.project.awesomegroup.dto.alarm.request.AlarmRequest;
import com.project.awesomegroup.dto.alarm.request.AlarmUpdateRequest;
import com.project.awesomegroup.dto.alarm.response.AlarmListResponse;
import com.project.awesomegroup.dto.alarm.response.AlarmResponse;
import com.project.awesomegroup.dto.teammember.response.TeamMemberResponse;
import com.project.awesomegroup.dto.user.response.UserResponse;
import com.project.awesomegroup.service.AlarmService;
import com.project.awesomegroup.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/alarms")
@Tag(name = "Alarm", description = "Alarm API")
public class AlarmController {

    @Autowired
    private AlarmService alarmService;

    @Operation(summary = "팀의 알람 조회", description = "teamId에 해당하는 알람들을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공 (message : \"Alarm Found\", code : 200)", content = @Content(schema = @Schema(implementation = AlarmListResponse.class))),
            @ApiResponse(responseCode = "404", description = "조회 실패 (message : \"Alarm not Found\", code : 404, data : null)", content = @Content)
    })
    @GetMapping("/{teamId}")
    public ResponseEntity<AlarmListResponse> alarmSelect(@PathVariable("teamId") Integer teamId) {
        AlarmListResponse response = alarmService.findByTeamId(teamId);
        if(response.getCode() == 404) {
            //정보를 찾지 못했을 때 (code = 404)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        //해당하는 ID 정보를 찾았을 때 (code = 200)
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "팀의 요일 알람 조회", description = "teamId와 요일에 해당하는 알람들을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공 (message : \"Alarm Found\", code : 200)", content = @Content(schema = @Schema(implementation = AlarmListResponse.class))),
            @ApiResponse(responseCode = "404", description = "조회 실패 (message : \"Alarm not Found\", code : 404, data : null)", content = @Content)
    })
    @GetMapping("/{teamId}/{alarmDay}")
    public ResponseEntity<AlarmListResponse> alarmSelect(@PathVariable("teamId") Integer teamId, @PathVariable("alarmDay") String alarmDay) {
        AlarmListResponse response = alarmService.findByTeamIdAndAlarmDay(teamId, alarmDay);
        if(response.getCode() == 404) {
            //정보를 찾지 못했을 때 (code = 404)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        //해당하는 ID 정보를 찾았을 때 (code = 200)
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "알람 생성", description = "알람 정보를 기입해 알람을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "알람 생성 성공 (message : \"Success\", code : 201)", content = @Content(schema = @Schema(implementation = AlarmResponse.class))),
            @ApiResponse(responseCode = "404", description = "알람 생성 실패 (message : \"Team not Found\", code : 404, data : null)", content = @Content),
            @ApiResponse(responseCode = "500", description = "알람 생성 실패 (message : \"Server Error\", code : 500, data : null)", content = @Content)
    })
    @PostMapping
    public ResponseEntity<AlarmResponse> alarmCreate(@RequestBody AlarmRequest request){
        AlarmResponse checkAlarm = alarmService.insert(request);
        if(checkAlarm.getCode() == 404){
            //회원 가입 실패 시 (팀이 존재하지 않음) (code = 404)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(checkAlarm);
        }
        if(checkAlarm.getCode() == 500){
            //알람 생성 실패 시 (서버 에러) (code = 500)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(checkAlarm);
        }
        //알람 생성 성공 시 (code = 201)
        return ResponseEntity.status(HttpStatus.CREATED).body(checkAlarm);
    }

    @Operation(summary = "알람 수정", description = "알람 alarmName, alarmDay 에 대해서만 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공 (message : \"Success\", code : 200)", content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "수정 실패 (message : \"Team not Found\", code : 404, data : null)\n" +
                    "\n" +
                    "수정 실패 (message : \"Alarm not Found\", code : 404, data : null)", content = @Content),
            @ApiResponse(responseCode = "500", description = "수정 실패 (message : \"Fail\", code : 500, data : null)", content = @Content)
    })
    @PutMapping
    public  ResponseEntity<AlarmResponse> alarmUpdate(@RequestBody AlarmUpdateRequest request){
        AlarmResponse response = alarmService.update(request);
        if (response.getCode() == 404) {
            //알람 생성 실패 시 (알람 또는 팀이 조회되지 않음) (code = 404)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        //성공적으로 수정이 되었을 때 (code = 200)
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "알람 삭제", description = "알람 아이디로 알람을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공 (string : \"Success\")", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "삭제 실패 (string : \"User not Found\")\n" +
                    "\n" +
                    "삭제 실패 (string : \"Team not Found\")", content = @Content),
    })
    @DeleteMapping("/{alarmId}")
    public ResponseEntity<String> alarmDelete(@PathVariable Integer alarmId){
        Map<String, String> map = alarmService.delete(alarmId);
        String response = map.get("result");
        if(response.equals("Success")) {
            //성공적으로 삭제가 되었을 때 (code = 200)
            return ResponseEntity.ok(response);
        }
        //삭제 실패 시 (code = 404)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
