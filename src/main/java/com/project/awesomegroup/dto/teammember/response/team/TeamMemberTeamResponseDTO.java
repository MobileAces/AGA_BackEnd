package com.project.awesomegroup.dto.teammember.response.team;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeamMemberTeamResponseDTO {

    private Integer teamId;
    private Integer authority;
}
