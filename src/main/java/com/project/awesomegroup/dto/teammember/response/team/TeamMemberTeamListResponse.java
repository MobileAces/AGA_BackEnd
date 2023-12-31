package com.project.awesomegroup.dto.teammember.response.team;

import com.project.awesomegroup.dto.team.Team;
import com.project.awesomegroup.dto.teammember.TeamMember;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Data
@Builder
public class TeamMemberTeamListResponse {


    @Schema(description = "상태 메세지", nullable = false, example = "Success")
    private String message;
    @Schema(description = "상태 코드", nullable = false, example = "200")
    private Integer code;

    //여러 팀이 저장될 수 있으므로 List 형식으로 선언
    @Schema(description = "데이터", nullable = true)
    private List<TeamMemberInfo> dataList;

    //생성 메소드//
    public static TeamMemberTeamListResponse createTeamMemberUserListResponse(String message, Integer code, List<TeamMemberInfo> dataList){
        return TeamMemberTeamListResponse.builder()
                .message(message)
                .code(code)
                .dataList(dataList)
                .build();
    }
}
