package com.project.awesomegroup.controller.alarm;

import com.project.awesomegroup.dto.alarm.request.AlarmRequest;
import com.project.awesomegroup.dto.alarm.request.AlarmUpdateRequest;
import com.project.awesomegroup.dto.alarm.response.AlarmDeleteResponse;
import com.project.awesomegroup.dto.alarm.response.AlarmListResponse;
import com.project.awesomegroup.dto.alarm.response.AlarmListWithDetailResponse;
import com.project.awesomegroup.dto.alarm.response.AlarmResponse;
import com.project.awesomegroup.dto.user.response.UserCheckResponse;
import com.project.awesomegroup.dto.user.response.UserResponse;
import com.project.awesomegroup.service.AlarmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/alarms")
@Tag(name = "Alarm", description = "Alarm API")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    @Operation(summary = "팀의 알람 조회", description = "teamId에 해당하는 알람들을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공 (message : \"Alarm Found\", code : 200)", content = @Content(schema = @Schema(implementation = AlarmListWithDetailResponse.class))),
            @ApiResponse(responseCode = "404", description = "조회 실패 (message : \"Alarm not Found\", code : 404, data : null)", content = @Content)
    })
    @GetMapping("/{teamId}")
    public ResponseEntity<AlarmListWithDetailResponse> alarmSelect(@PathVariable("teamId") Integer teamId) {
        return alarmService.findByTeamId(teamId);
    }

    @Operation(summary = "팀의 요일 알람 조회", description = "teamId와 요일에 해당하는 알람들을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공 (message : \"Alarm Found\", code : 200)", content = @Content(schema = @Schema(implementation = AlarmListResponse.class))),
            @ApiResponse(responseCode = "404", description = "조회 실패 (message : \"Alarm not Found\", code : 404, data : null)", content = @Content)
    })
    @GetMapping("/{teamId}/{alarmDay}")
    public ResponseEntity<AlarmListResponse> alarmSelect(@PathVariable("teamId") Integer teamId, @PathVariable("alarmDay") String alarmDay) {
        return alarmService.findByTeamIdAndAlarmDay(teamId, alarmDay);
    }

    @Operation(summary = "알람 생성", description = "알람 정보를 기입해 알람을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "알람 생성 성공 (message : \"Success\", code : 201)", content = @Content(schema = @Schema(implementation = AlarmResponse.class))),
            @ApiResponse(responseCode = "404", description = "알람 생성 실패 (message : \"Team not Found\", code : 404, data : null)", content = @Content),
            @ApiResponse(responseCode = "500", description = "알람 생성 실패 (message : \"Server Error\", code : 500, data : null)", content = @Content)
    })
    @PostMapping
    public ResponseEntity<AlarmResponse> alarmCreate(@RequestBody AlarmRequest request){
        return alarmService.insert(request);
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
        return alarmService.update(request);
    }

    @Operation(summary = "알람 삭제", description = "알람 아이디로 알람을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공 (message : \"Success\", code : 200, data : true)", content = @Content(schema = @Schema(implementation = AlarmDeleteResponse.class))),
            @ApiResponse(responseCode = "404", description = "삭제 실패 (message : \"User not Found\", code : 404, data : false)\n" +
                    "\n" +
                    "삭제 실패 (string : \"Team not Found\"), code : 404, data : false)", content = @Content),
            @ApiResponse(responseCode = "400", description = "삭제 실패 (message : \"Fail\", code : 400, data : false)", content = @Content)
    })
    @DeleteMapping("/{alarmId}")
    public ResponseEntity<AlarmDeleteResponse> alarmDelete(@PathVariable Integer alarmId){
        return alarmService.delete(alarmId);
    }
}
