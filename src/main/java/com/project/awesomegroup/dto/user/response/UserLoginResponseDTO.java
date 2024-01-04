package com.project.awesomegroup.dto.user.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginResponseDTO {
    private String userId;

    public static UserLoginResponseDTO createUserLoginResponseDTO(String userId){
        return UserLoginResponseDTO.builder()
                .userId(userId)
                .build();
    }
}
