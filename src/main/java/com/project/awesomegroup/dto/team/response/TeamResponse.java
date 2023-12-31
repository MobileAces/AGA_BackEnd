package com.project.awesomegroup.dto.team.response;


import com.project.awesomegroup.dto.team.Team;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeamResponse {

    @Schema(description = "상태 메세지", nullable = false, example = "Success")
    private String message;
    @Schema(description = "상태 코드", nullable = false, example = "200")
    private Integer code;
    @Schema(description = "데이터", nullable = true)
    private Team data;

    //생성 메소드//
    public static TeamResponse createTeamResponse(String message, Integer code, Team data){
        return TeamResponse.builder()
                .message(message)
                .code(code)
                .data(data)
                .build();
    }
}
