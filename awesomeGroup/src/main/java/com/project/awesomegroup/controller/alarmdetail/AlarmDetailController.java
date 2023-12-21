package com.project.awesomegroup.controller.alarmdetail;

import com.project.awesomegroup.dto.alarmdetail.AlarmDetail;
import com.project.awesomegroup.dto.alarmdetail.AlarmDetailRequest;
import com.project.awesomegroup.dto.alarmdetail.AlarmDetailResponse;
import com.project.awesomegroup.service.AlarmDetailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("/personalAlarms")
@Tag(name = "AlarmDetail", description = "AlarmDetail API")
public class AlarmDetailController {

    @Autowired
    private AlarmDetailService alarmDetailService;

    @GetMapping("/entirety")
    public List<AlarmDetailResponse> selectAll() {
        List<AlarmDetailResponse> responseList = alarmDetailService.selectAll().stream()
                .map(AlarmDetailResponse::createAlarmDetailResponse)
                .collect(Collectors.toList());
        return responseList;
    }

    @GetMapping("/team/{alarm_id}")
    public List<AlarmDetailResponse> selectTeamAlarmList(@PathVariable Integer alarm_id) {
        List<AlarmDetailResponse> responseList = alarmDetailService.selectByAlarmId(alarm_id).stream()
                .map(AlarmDetailResponse::createAlarmDetailResponse)
                .collect(Collectors.toList());
        return responseList;
    }

    @GetMapping("/{alarmDetail_id}")
    public AlarmDetailResponse select(@PathVariable Integer alarmDetail_id) {
        AlarmDetail findAlarmDetail = alarmDetailService.selectByAlarmDetailId(alarmDetail_id);
        AlarmDetailResponse response = AlarmDetailResponse.createAlarmDetailResponse(findAlarmDetail);
        return response;
    }

    @PostMapping
    public AlarmDetail insert(@RequestBody AlarmDetailRequest request) {
        AlarmDetail alarmDetail = alarmDetailService.insert(request);
//        AlarmDetailResponse response = AlarmDetailResponse.createAlarmDetailResponse(alarmDetail);
        return alarmDetail;
    }
}
