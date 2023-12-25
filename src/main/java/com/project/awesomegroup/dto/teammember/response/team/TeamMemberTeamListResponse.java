package com.project.awesomegroup.dto.teammember.response.team;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TeamMemberTeamListResponse {


    @Schema(description = "상태 메세지", nullable = false, example = "Success")
    private String message;
    @Schema(description = "상태 코드", nullable = false, example = "200")
    private Integer code;
    @Schema(description = "유저 아이디", nullable = false)
    private String userId;
    @Schema(description = "데이터", nullable = true)
    private List<TeamMemberTeamResponseDTO> dataList;

    //생성 메소드//
    public static TeamMemberTeamListResponse createTeamMemberUserListResponse(String message, Integer code, String userId, List<TeamMemberTeamResponseDTO> dataList){
        return TeamMemberTeamListResponse.builder()
                .message(message)
                .code(code)
                .userId(userId)
                .dataList(dataList)
                .build();
    }
}
