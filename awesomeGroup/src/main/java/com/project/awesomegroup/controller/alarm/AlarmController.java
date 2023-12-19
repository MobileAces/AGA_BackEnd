package com.project.awesomegroup.controller.alarm;

import com.project.awesomegroup.dto.Alarm;
import com.project.awesomegroup.dto.Team;
import com.project.awesomegroup.dto.TeamMember;
import com.project.awesomegroup.service.AlarmService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/alarms")
@Tag(name = "Alarm", description = "Alarm API")
public class AlarmController {

    @Autowired
    private AlarmService alarmService;

    @GetMapping("/{teamId}")
    public List<Alarm> alarmSelect(@PathVariable("teamId") Integer teamId) {
        return alarmService.findByTeamId(teamId);
    }

    @GetMapping("/{teamId}/{alarmDay}")
    public List<Alarm> alarmSelect(@PathVariable("teamId") Integer teamId, @PathVariable("alarmDay") String alarmDay) {
        return alarmService.findByTeamIdAndAlarmDay(teamId, alarmDay);
    }

    @PostMapping
    public Alarm alarmCreate(@RequestBody Alarm alarm){
        return alarmService.insert(alarm);
    }

    @PutMapping
    public boolean teamUpdate(@RequestBody Alarm alarm){
        return alarmService.update(alarm);
    }

    @DeleteMapping("/{alarmId}")
    public boolean teamDelete(@PathVariable Integer alarmId){
        return alarmService.delete(alarmId);
    }
}
