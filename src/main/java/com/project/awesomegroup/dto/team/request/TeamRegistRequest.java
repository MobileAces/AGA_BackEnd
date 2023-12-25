package com.project.awesomegroup.dto.team.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeamRegistRequest {

    private String teamName;

    private String teamInfo;

    private String teamMaster;
}
