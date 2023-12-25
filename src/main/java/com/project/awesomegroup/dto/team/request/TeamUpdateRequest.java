package com.project.awesomegroup.dto.team.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeamUpdateRequest {

    private Integer teamId;

    private String teamName;

    private String teamInfo;

    private String teamMaster;

}
