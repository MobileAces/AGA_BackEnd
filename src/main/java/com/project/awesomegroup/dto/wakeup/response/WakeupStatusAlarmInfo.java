package com.project.awesomegroup.dto.wakeup.response;

import com.project.awesomegroup.dto.wakeup.Wakeup;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WakeupStatusAlarmInfo {
    private Integer alarmId;
    private String alarmName;
    private List<WakeupStatusResponseDTO> wakeupList;

    public static WakeupStatusAlarmInfo createWakeupStatusResponseDTO(Integer alarmId, String alarmName, List<WakeupStatusResponseDTO> wakeupList) {
        return WakeupStatusAlarmInfo.builder()
                .alarmId(alarmId)
                .alarmName(alarmName)
                .wakeupList(wakeupList)
                .build();
    }
}
