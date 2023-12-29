package com.project.awesomegroup.controller.wakeup;

import com.project.awesomegroup.dto.wakeup.Wakeup;
import com.project.awesomegroup.dto.wakeup.WakeupDTO;
import com.project.awesomegroup.service.WakeupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wakeup")
public class WakeupController {
    @Autowired
    WakeupService wakeupService;

    @PostMapping("/register")
    public ResponseEntity<Wakeup> registerWakeup(@RequestBody Wakeup wakeupDTO) {
        return ResponseEntity.ok(wakeupService.registerWakeup(wakeupDTO));
    }

    @GetMapping("/status")
    public ResponseEntity<List<WakeupDTO>> getWakeupStatusByTeamAndDate(@RequestParam Integer teamId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        List<WakeupDTO> wakeupStatusList = wakeupService.getWakeupStatusByTeamAndDate(teamId, date);
        return ResponseEntity.ok(wakeupStatusList);
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Integer>> getWakeupStatisticsByTeamAndDateRange(
            @RequestParam Integer teamId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

        Map<String, Integer> wakeupStatistics = wakeupService.getWakeupStatisticsByTeamAndDateRange(teamId, startDate, endDate);
        return ResponseEntity.ok(wakeupStatistics);
    }
}
