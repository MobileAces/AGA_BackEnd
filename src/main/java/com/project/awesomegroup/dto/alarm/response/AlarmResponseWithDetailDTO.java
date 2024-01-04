package com.project.awesomegroup.dto.alarm.response;

import com.project.awesomegroup.dto.alarmdetail.response.AlarmDetailResponseDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AlarmResponseWithDetailDTO {

    private Integer alarmId;

    private String alarmName;

    private String alarmDay;

    private Integer teamId;

    private List<AlarmDetailResponseDTO> dataList;

}
