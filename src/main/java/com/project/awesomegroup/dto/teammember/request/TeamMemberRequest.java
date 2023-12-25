package com.project.awesomegroup.dto.teammember.request;

import com.project.awesomegroup.dto.teammember.TeamMember;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeamMemberRequest {

    private Integer teamId;

    private String userId;

    private int authority;

    public static TeamMemberRequest createTeamMemberRequest(TeamMember teamMember) {
        TeamMemberRequest teamMemberRequest = TeamMemberRequest.builder()
                .teamId(teamMember.getTeam().getTeamId())
                .userId(teamMember.getUser().getUserId())
                .authority(teamMember.getAuthority())
                .build();
        return teamMemberRequest;
    }
}
