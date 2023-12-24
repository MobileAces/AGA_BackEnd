package com.project.awesomegroup.dto.user.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDTO {
    private String userId;
    private String userNickname;
    private String userPhone;

    public UserResponseDTO(String userId, String userNickname, String userPhone){
        this.userId = userId;
        this.userNickname = userNickname;
        this.userPhone = userPhone;
    }
}
