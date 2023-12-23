package com.project.awesomegroup.controller.alarm;

import com.project.awesomegroup.dto.alarm.Alarm;
import com.project.awesomegroup.dto.alarm.AlarmResponse;
import com.project.awesomegroup.service.AlarmService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("/alarms")
@Tag(name = "Alarm", description = "Alarm API")
public class AlarmController {

    @Autowired
    private AlarmService alarmService;

    @GetMapping("/{teamId}")
    public List<AlarmResponse> alarmSelect(@PathVariable("teamId") Integer teamId) {
        List<AlarmResponse> responseDTOList = alarmService.findByTeamId(teamId).stream()
                .map(AlarmResponse::createAlarmResponse)
                .collect(Collectors.toList());
        return responseDTOList;
    }

    @GetMapping("/{teamId}/{alarmDay}")
    public List<AlarmResponse> alarmSelect(@PathVariable("teamId") Integer teamId, @PathVariable("alarmDay") String alarmDay) {
        List<AlarmResponse> responseDTOList = alarmService.findByTeamIdAndAlarmDay(teamId, alarmDay).stream()
                .map(AlarmResponse::createAlarmResponse)
                .collect(Collectors.toList());
        return responseDTOList;
    }

    @PostMapping
    public Alarm alarmCreate(@RequestBody Alarm alarm){
        return alarmService.insert(alarm);
    }

    @PutMapping
    public boolean alarmUpdate(@RequestBody Alarm alarm){
        return alarmService.update(alarm);
    }

    @DeleteMapping("/{alarmId}")
    public boolean alarmDelete(@PathVariable Integer alarmId){
        return alarmService.delete(alarmId);
    }
}
