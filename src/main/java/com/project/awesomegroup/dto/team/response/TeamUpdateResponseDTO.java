package com.project.awesomegroup.dto.team.response;

import com.project.awesomegroup.dto.team.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TeamUpdateResponseDTO {

    private Integer teamId;

    private String teamName;

    private String teamInfo;

    private String teamMaster;

    //생성 메소드
    public static TeamUpdateResponseDTO createTeamUpdateResponse(Team team, String teamName, String teamInfo, String teamMaster){
        return TeamUpdateResponseDTO.builder()
                .teamId(team.getTeamId())
                .teamName(teamName)
                .teamInfo(teamInfo)
                .teamMaster(teamMaster)
                .build();
    }
}
