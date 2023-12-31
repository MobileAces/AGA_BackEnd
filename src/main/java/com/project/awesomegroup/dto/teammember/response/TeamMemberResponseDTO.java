package com.project.awesomegroup.dto.teammember.response;

import com.project.awesomegroup.dto.teammember.TeamMember;
import com.project.awesomegroup.dto.user.response.UserResponse;
import com.project.awesomegroup.dto.user.response.UserResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeamMemberResponseDTO {

    private Integer teamId;

    private String userId;

    private int authority;

    public static TeamMemberResponseDTO createTeamMemberResponse(TeamMember teamMember) {
        TeamMemberResponseDTO teamMemberResponseDTO = TeamMemberResponseDTO.builder()
                .teamId(teamMember.getTeam().getTeamId())
                .userId(teamMember.getUser().getUserId())
                .authority(teamMember.getAuthority())
                .build();
        return teamMemberResponseDTO;
    }
}
