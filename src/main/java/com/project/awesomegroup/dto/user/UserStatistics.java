package com.project.awesomegroup.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserStatistics {
    private String nickname;
    private int totalSum;
    private int totalSuccessSum;
}
