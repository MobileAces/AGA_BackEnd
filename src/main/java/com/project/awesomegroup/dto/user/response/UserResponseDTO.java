package com.project.awesomegroup.dto.user.response;


import com.project.awesomegroup.dto.user.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDTO {
    private String userId;
    private String userNickname;
    private String userPhone;

    //생성 메소드
    public static UserResponseDTO createUserResponseDTO(User user){
        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .userNickname(user.getUserNickname())
                .userPhone(user.getUserPhone())
                .build();
    }

    public static UserResponseDTO createUserResponseDTO(User user, String userNickname, String userPhone){
        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .userNickname(user.getUserNickname())
                .userPhone(user.getUserPhone())
                .build();
    }
}
