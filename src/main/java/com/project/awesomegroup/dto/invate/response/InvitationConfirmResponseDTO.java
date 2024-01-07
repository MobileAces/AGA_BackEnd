package com.project.awesomegroup.dto.invate.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InvitationConfirmResponseDTO {
    private Integer teamId;

    public static InvitationConfirmResponseDTO createInvitationGenerateResponseDTO(Integer teamId){
        return InvitationConfirmResponseDTO.builder()
                .teamId(teamId)
                .build();
    }
}
