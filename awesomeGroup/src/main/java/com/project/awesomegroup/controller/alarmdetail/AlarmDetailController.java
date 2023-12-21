package com.project.awesomegroup.controller.alarmdetail;

import com.project.awesomegroup.dto.alarmdetail.AlarmDetail;
import com.project.awesomegroup.service.AlarmDetailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/alarmdetails")
@Tag(name = "AlarmDetail", description = "AlarmDetail API")
public class AlarmDetailController {

    @Autowired
    private AlarmDetailService alarmDetailService;

    @GetMapping("/entirety")
    public List<AlarmDetail> alarmDetailAll() {
        return alarmDetailService.selectAll();
    }

    @GetMapping("/{alarm_id}")
    public List<AlarmDetail> alarmDetailSelect(@PathVariable Integer alarm_id) {
        return alarmDetailService.selectByAlarmId(alarm_id);
    }
}
