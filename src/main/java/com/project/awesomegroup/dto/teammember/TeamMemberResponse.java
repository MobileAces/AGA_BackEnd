package com.project.awesomegroup.dto.teammember;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeamMemberResponse {

    private Integer teammemberId;

    private Integer teamId;

    private String userId;

    private int authority;

    public static TeamMemberResponse createTeamMemberResponse(TeamMember teamMember) {
        TeamMemberResponse teamMemberResponse = TeamMemberResponse.builder()
                .teammemberId(teamMember.getId())
                .teamId(teamMember.getTeam().getTeamId())
                .userId(teamMember.getUser().getUserId())
                .authority(teamMember.getAuthority())
                .build();
        return teamMemberResponse;
    }
}
