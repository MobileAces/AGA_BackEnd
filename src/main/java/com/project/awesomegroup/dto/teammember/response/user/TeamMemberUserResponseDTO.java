package com.project.awesomegroup.dto.teammember.response.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeamMemberUserResponseDTO {

    private String userId;
    private Integer authority;

}
