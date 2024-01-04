package com.project.awesomegroup.controller.alarmdetail;

import com.project.awesomegroup.dto.alarm.response.AlarmListResponse;
import com.project.awesomegroup.dto.alarmdetail.AlarmDetail;
import com.project.awesomegroup.dto.alarmdetail.request.AlarmDetailRequest;
import com.project.awesomegroup.dto.alarmdetail.request.AlarmDetailUpdateRequest;
import com.project.awesomegroup.dto.alarmdetail.response.AlarmDetailListResponse;
import com.project.awesomegroup.dto.alarmdetail.response.AlarmDetailResponse;
import com.project.awesomegroup.dto.alarmdetail.response.AlarmDetailResponseDTO;
import com.project.awesomegroup.dto.teammember.response.TeamMemberResponse;
import com.project.awesomegroup.service.AlarmDetailService;
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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("/personalAlarms")
@Tag(name = "AlarmDetail", description = "AlarmDetail API")
@RequiredArgsConstructor
public class AlarmDetailController {

    private final AlarmDetailService alarmDetailService;

    @Operation(summary = "개인알람 전체 조회", description = "모든 개인알람의 정보를 반환합니다.")
    @GetMapping("/entirety")
    public List<AlarmDetailResponseDTO> selectAll() {
        List<AlarmDetailResponseDTO> responseList = alarmDetailService.selectAll().stream()
                .map(AlarmDetailResponseDTO::createAlarmDetailResponseDTO)
                .collect(Collectors.toList());
        return responseList;
    }

    @Operation(summary = "팀 알람의 개인알람 조회", description = "alarmId 해당하는 개인알람들을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "팀알람의 개인알람 조회 성공 (message : \"AlarmDetail Found\", code : 200)", content = @Content(schema = @Schema(implementation = AlarmDetailListResponse.class))),
            @ApiResponse(responseCode = "404", description = "팀알람의 개인알람 조회 실패 (message : \"AlarmDetail not Found\", code : 404, data : null)", content = @Content)
    })
    @GetMapping("/team/{alarmId}")
    public ResponseEntity<AlarmDetailListResponse> selectTeamAlarmList(@PathVariable Integer alarmId) {
        AlarmDetailListResponse response = alarmDetailService.selectByAlarmId(alarmId);
        if(response.getCode() == 404) {
            //정보를 찾지 못했을 때 (code = 404)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        //해당하는 ID 정보를 찾았을 때 (code = 200)
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "개인알람 조회", description = "개인 알람 아이디에 해당하는 알람을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공 (message : \"AlarmDetail Found\", code : 200)", content = @Content(schema = @Schema(implementation = AlarmDetailResponse.class))),
            @ApiResponse(responseCode = "404", description = "조회 실패 (message : \"AlarmDetail not Found\", code : 404, data : null)", content = @Content)
    })
    @GetMapping("/{alarmDetailId}")
    public ResponseEntity<AlarmDetailResponse> select(@PathVariable Integer alarmDetailId) {
        AlarmDetailResponse response = alarmDetailService.selectByAlarmDetailId(alarmDetailId);
        if(response.getCode() == 404) {
            //정보를 찾지 못했을 때 (code = 404)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        //해당하는 ID 정보를 찾았을 때 (code = 200)
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "개인알람 등록", description = "개인알람에 해당하는 속성값을 기입해 개인알람을 저장합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "개인알람 등록 성공 (message : \"Success\", code : 201)", content = @Content(schema = @Schema(implementation = AlarmDetailResponse.class))),
            @ApiResponse(responseCode = "404", description = "개인알람 등록 실패 (message : \"User not Found\", code : 404, data : null)\n" +
                    "\n" +
                    "개인알람 등록 실패 (message : \"Alarm not Found\", code : 404, data : null)\n" +
                    "\n" +
                    "개인알람 등록 실패 (message : \"User does not exist in the Team\", code : 404, data : null)", content = @Content),
    })
    @PostMapping
    public ResponseEntity<AlarmDetailResponse> insert(@RequestBody AlarmDetailRequest request) {
        AlarmDetailResponse alarmDetail = alarmDetailService.insert(request);
        if(alarmDetail.getCode() == 404){
            //개인알람 등록 실패 시 (code = 404)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(alarmDetail);
        }
        //개인알람 등록 성공 시 (code = 201)
        return ResponseEntity.ok(alarmDetail);
    }

    @Operation(summary = "개인알람 수정", description = "alarmDetailId 기준으로 개인알람 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "개인알람 수정 성공 (message : \"Success\", code : 200)", content = @Content(schema = @Schema(implementation = AlarmDetailResponse.class))),
            @ApiResponse(responseCode = "404", description = "개인알람 수정 실패 (message : \"User not Found\", code : 404, data : null)\n" +
                    "\n" +
                    "개인알람 수정 실패 (message : \"Alarm not Found\", code : 404, data : null)\n" +
                    "\n" +
                    "개인알람 수정 실패 (message : \"User does not exist in the Team\", code : 404, data : null)", content = @Content),
            @ApiResponse(responseCode = "500", description = "개인알람 수정 실패 (message : \"Server Error\", code : 500, data : null)", content = @Content)
    })
    @PutMapping
    public ResponseEntity<AlarmDetailResponse> update(@RequestBody AlarmDetailUpdateRequest request) {
        AlarmDetailResponse alarmDetail = alarmDetailService.update(request);
        if(alarmDetail.getCode() == 404){
            //개인알람 등록 실패 시 (code = 404)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(alarmDetail);
        } else if (alarmDetail.getCode() == 500) {
            //개인알람 등록 실패 시 (code = 500)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(alarmDetail);
        }
        //개인알람 등록 성공 시 (code = 200)
        return ResponseEntity.ok(alarmDetail);
    }

    @Operation(summary = "개인알람 삭제", description = "alarmDetailId 기준으로 개인알람을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공 (string : \"Success\")", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "삭제 실패 (string : \"User not Found\")\n" +
                    "\n" +
                    "삭제 실패 (string : \"Team not Found\")", content = @Content),
    })
    @DeleteMapping("/{alarmDetailId}")
    public ResponseEntity<String> delete(@PathVariable Integer alarmDetailId) {

        Map<String, String> map = alarmDetailService.delete(alarmDetailId);
        String response = map.get("result");
        if(response.equals("Success")) {
            //성공적으로 삭제가 되었을 때 (code = 200)
            return ResponseEntity.ok(response);
        }
        //삭제 실패 시 (code = 404)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
