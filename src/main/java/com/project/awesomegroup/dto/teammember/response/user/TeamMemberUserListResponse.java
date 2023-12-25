package com.project.awesomegroup.dto.teammember.response.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TeamMemberUserListResponse {   //Team 에 속해 있는 유저 리스트 반환

    @Schema(description = "상태 메세지", nullable = false, example = "Success")
    private String message;
    @Schema(description = "상태 코드", nullable = false, example = "200")
    private Integer code;
    @Schema(description = "팀 고유번호", nullable = false)
    private Integer teamId;
    @Schema(description = "데이터", nullable = true)
    private List<TeamMemberUserResponseDTO> dataList;

    //생성 메소드//
    public static TeamMemberUserListResponse createTeamMemberUserListResponse(String message, Integer code, Integer teamId, List<TeamMemberUserResponseDTO> dataList){
        return TeamMemberUserListResponse.builder()
                .message(message)
                .code(code)
                .teamId(teamId)
                .dataList(dataList)
                .build();
    }

}
