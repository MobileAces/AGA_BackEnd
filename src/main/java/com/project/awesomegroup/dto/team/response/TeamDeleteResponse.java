package com.project.awesomegroup.dto.team.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeamDeleteResponse {
    @Schema(description = "상태 메세지", nullable = false, example = "Success")
    private String message;
    @Schema(description = "상태 코드", nullable = false, example = "200")
    private Integer code;
    @Schema(description = "데이터", nullable = false)
    private TeamDeleteResponseDTO data;

    //생성 메소드//
    public static TeamDeleteResponse TeamDeleteResponseCreate(String message, Integer code, TeamDeleteResponseDTO data){
        return TeamDeleteResponse.builder()
                .message(message)
                .code(code)
                .data(data)
                .build();
    }
}
