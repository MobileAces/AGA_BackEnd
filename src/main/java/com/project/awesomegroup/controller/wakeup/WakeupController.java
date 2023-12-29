package com.project.awesomegroup.controller.wakeup;

import com.project.awesomegroup.dto.user.UserStatistics;
import com.project.awesomegroup.dto.wakeup.WakeupDTO;
import com.project.awesomegroup.dto.wakeup.request.WakeupSaveRequest;
import com.project.awesomegroup.dto.wakeup.request.WakeupStatisticsRequest;
import com.project.awesomegroup.dto.wakeup.response.WakeupResponse;
import com.project.awesomegroup.dto.wakeup.response.WakeupStatisticsResponse;
import com.project.awesomegroup.service.WakeupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/wakeup")
public class WakeupController {
    private final Logger logger = LoggerFactory.getLogger(WakeupController.class);

    @Autowired
    WakeupService wakeupService;

    @PostMapping("/register")
    public ResponseEntity<WakeupResponse> registerWakeup(@RequestBody WakeupSaveRequest request) {
        WakeupResponse response = wakeupService.registerWakeup(request);
        if(response.getCode() == 404) {
            //정보를 찾지 못했을 때 (code = 404)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        //해당하는 ID 정보를 찾았을 때 (code = 200)
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    public ResponseEntity<List<WakeupDTO>> getWakeupStatusByTeamAndDate(@RequestParam Integer teamId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        List<WakeupDTO> wakeupStatusList = wakeupService.getWakeupStatusByTeamAndDate(teamId, date);
        return ResponseEntity.ok(wakeupStatusList);
    }

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
