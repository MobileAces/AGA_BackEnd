package com.project.awesomegroup.dto.teammember.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeamMemberDeleteResponseDTO {
    private boolean result;

    public static TeamMemberDeleteResponseDTO teamMemberDeleteResponseDTOCreate(boolean result){
        return TeamMemberDeleteResponseDTO.builder()
                .result(result)
                .build();
    }
}
