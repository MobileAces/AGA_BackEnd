package com.project.awesomegroup.dto.invate.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InvitationConfirmResponse {
    @Schema(description = "상태 메세지", nullable = false, example = "Success")
    private String message;
    @Schema(description = "상태 코드", nullable = false, example = "200")
    private Integer code;
    @Schema(description = "데이터", nullable = false)
    private InvitationConfirmResponseDTO data;

    public static InvitationConfirmResponse createInvitationConfirmResponse(String message, Integer code, InvitationConfirmResponseDTO data){
        return InvitationConfirmResponse.builder()
                .message(message)
                .code(code)
                .data(data)
                .build();
    }
}
