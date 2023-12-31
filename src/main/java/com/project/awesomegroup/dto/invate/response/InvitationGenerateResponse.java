package com.project.awesomegroup.dto.invate.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InvitationGenerateResponse {
    @Schema(description = "상태 메세지", nullable = false, example = "Success")
    private String message;
    @Schema(description = "상태 코드", nullable = false, example = "200")
    private Integer code;
    @Schema(description = "데이터", nullable = true)
    private InvitationGenerateResponseDTO data;

    public static InvitationGenerateResponse createInvitationGenerateResponse(String message, Integer code, InvitationGenerateResponseDTO data){
        return InvitationGenerateResponse.builder()
                .message(message)
                .code(code)
                .data(data)
                .build();
    }
}
