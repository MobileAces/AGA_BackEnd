package com.project.awesomegroup.controller.wakeup;

import com.project.awesomegroup.dto.alarmdetail.response.AlarmDetailResponse;
import com.project.awesomegroup.dto.wakeup.request.WakeupSaveRequest;
import com.project.awesomegroup.dto.wakeup.request.WakeupStatisticsRequest;
import com.project.awesomegroup.dto.wakeup.response.WakeupResponse;
import com.project.awesomegroup.dto.wakeup.response.WakeupStatisticsResponse;
import com.project.awesomegroup.dto.wakeup.response.WakeupStatusResponse;
import com.project.awesomegroup.service.WakeupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@RestController
@RequestMapping("/wakeup")
@RequiredArgsConstructor
public class WakeupController {
    private final Logger logger = LoggerFactory.getLogger(WakeupController.class);

    private final WakeupService wakeupService;

    @Operation(summary = "알람기록 저장", description = "해당 날짜에 개인알람의 속성값을 알람기록(wakeup)으로 저장합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "알람기록 저장 성공 (message : \"Save Success\", code : 201)", content = @Content(schema = @Schema(implementation = AlarmDetailResponse.class))),
            @ApiResponse(responseCode = "404", description = "알람기록 저장 실패 (message : \"User not Found\", code : 404, data : null)\n" +
                    "\n" +
                    "알람기록 저장 실패 (message : \"Team not Found\", code : 404, data : null)\n" +
                    "\n" +
                    "알람기록 저장 실패 (message : \"Alarm not Found\", code : 404, data : null)\n" +
                    "\n" +
                    "알람기록 저장 실패 (message : \"AlarmDetail not Found\", code : 404, data : null)\n" +
                    "\n" +
                    "알람기록 저장 실패 (message : \"User does not exist in the Team\", code : 404, data : null)", content = @Content),
            @ApiResponse(responseCode = "409", description = "알람기록 저장 실패 (message : \"No match userId\", code : 409, data : null)\n" +
                    "\n" +
                    "알람기록 저장 실패 (message : \"No match teamId\", code : 409, data : null)\n" +
                    "\n" +
                    "알람기록 저장 실패 (message : \"No match alarmId\", code : 409, data : null)", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<WakeupResponse> registerWakeup(@RequestBody WakeupSaveRequest request) {
        return wakeupService.registerWakeup(request);
    }

    @Operation(summary = "하루 알람기록 조회", description = "팀 아이디와 해당하는 날짜에 따른 알람 기록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공 (message : \"Wakeup Found\", code : 200)", content = @Content(schema = @Schema(implementation = WakeupStatusResponse.class))),
            @ApiResponse(responseCode = "404", description = "조회 실패 (message : \"Team not Found\", code : 404, data : null)\n" +
                    "\n" +
                    "조회 실패 (message : \"Alarm not Found\", code : 404, data : null)\n" +
                    "\n" +
                    "조회 실패 (message : \"Wakeup not Found\", code : 404, data : null)", content = @Content)
    })


//    @GetMapping("/status")
//    public ResponseEntity<WakeupStatusResponse> getWakeupStatusByTeamAndDate(@RequestParam Integer teamId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime date) {
//        return wakeupService.getWakeupStatusByTeamAndDate(teamId, date);
//    }
    @GetMapping("/status")
    public ResponseEntity<WakeupStatusResponse> getWakeupStatusByTeamAndDate(
            @RequestParam Integer teamId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        return wakeupService.getWakeupStatusByTeamAndDate(teamId, date);
    }

    @Operation(summary = "기간 알람기록 조회", description = "팀 아이디와 해당하는 기간, 유저 명단에 따른 알람 기록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공 (message : \"Success\", code : 200)", content = @Content(schema = @Schema(implementation = WakeupStatisticsResponse.class))),
            @ApiResponse(responseCode = "404", description = "조회 실패 (message : \"User not Found\", code : 404, data : null)\n" +
                    "\n" +
                    "조회 실패 (message : \"Team not Found\", code : 404, data : null)" +
                    "\n" +
                    "조회 실패 (message : \"No data\", code : 404, data : null)", content = @Content)
    })
    @PostMapping("/statistics")
    public ResponseEntity<WakeupStatisticsResponse> getWakeupStatisticsByTeamAndDateRange(@RequestBody WakeupStatisticsRequest request) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = new Date();
        Date endDate = new Date();
        try {
            startDate = dateFormat.parse(request.getStartDate());
            endDate = dateFormat.parse(request.getEndDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return wakeupService.getWakeupStatisticsByTeamAndDateRange(request.getUserNicknameList(), request.getTeamId(), startDate, endDate);
    }
}
