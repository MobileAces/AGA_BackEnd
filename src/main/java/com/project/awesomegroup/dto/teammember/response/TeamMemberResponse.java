package com.project.awesomegroup.dto.teammember.response;

import com.project.awesomegroup.dto.user.response.UserResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamMemberResponse {
    @Schema(description = "상태 메세지", nullable = false, example = "Success")
    private String message;
    @Schema(description = "상태 코드", nullable = false, example = "200")
    private Integer code;
    @Schema(description = "데이터", nullable = true)
    private TeamMemberResponseDTO data;

    //생성 메소드//
    public static TeamMemberResponse createTeamMemberResponseDTO(String message, Integer code, TeamMemberResponseDTO data){
        return TeamMemberResponse.builder()
                .message(message)
                .code(code)
                .data(data)
                .build();
    }
}
