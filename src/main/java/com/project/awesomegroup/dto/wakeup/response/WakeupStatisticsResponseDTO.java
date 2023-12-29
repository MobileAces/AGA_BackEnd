package com.project.awesomegroup.dto.wakeup.response;

import com.project.awesomegroup.dto.user.UserStatistics;
import com.project.awesomegroup.dto.wakeup.Wakeup;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class WakeupStatisticsResponseDTO {

    private List<UserStatistics> userList;
    private int totalSum;
    private int totalSuccessSum;

    public static WakeupStatisticsResponseDTO createWakeupResponseDTO (List<UserStatistics> userStatisticsList, int totalSum, int totalSuccessSum) {
        return WakeupStatisticsResponseDTO.builder()
                .userList(userStatisticsList)
                .totalSum(totalSum)
                .totalSuccessSum(totalSuccessSum)
                .build();
    }
}
