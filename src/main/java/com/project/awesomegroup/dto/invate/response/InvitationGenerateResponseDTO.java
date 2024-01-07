package com.project.awesomegroup.dto.invate.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InvitationGenerateResponseDTO {
    private String inviteCode;

    public static InvitationGenerateResponseDTO createInvitationGenerateResponseDTO(String inviteCode){
        return InvitationGenerateResponseDTO.builder()
                .inviteCode(inviteCode)
                .build();
    }
}
