package com.project.awesomegroup.dto.wakeup.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WakeupStatisticsRequest {

    private List<String> userNicknameList;
    private Integer teamId;
    private String startDate;
    private String endDate;
}
