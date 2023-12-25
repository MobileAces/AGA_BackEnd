package com.project.awesomegroup.dto.team.response;

import com.project.awesomegroup.dto.team.request.TeamUpdateRequest;
import com.project.awesomegroup.dto.user.response.UserResponse;
import com.project.awesomegroup.dto.user.response.UserResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeamUpdateResponse {
    @Schema(description = "상태 메세지", nullable = false, example = "Success")
    private String message;
    @Schema(description = "상태 코드", nullable = false, example = "200")
    private Integer code;
    @Schema(description = "데이터", nullable = true)
    private TeamUpdateResponseDTO data;

    //생성 메소드//
    public static TeamUpdateResponse createTeamUpdateResponse(String message, Integer code, TeamUpdateResponseDTO data){
        return TeamUpdateResponse.builder()
                .message(message)
                .code(code)
                .data(data)
                .build();
    }
}
