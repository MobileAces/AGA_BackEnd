package com.project.awesomegroup.dto.user.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private String message;
    private Integer code;
    private UserResponseDTO data;

    //생성 메소드//
    public static UserResponse userResponseCreate(String message, Integer code, UserResponseDTO data){
        return UserResponse.builder()
                .message(message)
                .code(code)
                .data(data)
                .build();
    }
}
