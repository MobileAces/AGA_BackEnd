package com.project.awesomegroup.dto.team.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeamUpdateResponseDTO {

    private Integer teamId;

    private String teamName;

    private String teamInfo;

    private String teamMaster;
}
