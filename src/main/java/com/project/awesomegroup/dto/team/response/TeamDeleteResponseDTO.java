package com.project.awesomegroup.dto.team.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeamDeleteResponseDTO {
    private boolean result;

    public static TeamDeleteResponseDTO createTeamDeleteResponseDTO(boolean result){
        return TeamDeleteResponseDTO.builder()
                .result(result)
                .build();
    }
}
