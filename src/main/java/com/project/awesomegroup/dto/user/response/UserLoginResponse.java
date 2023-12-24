package com.project.awesomegroup.dto.user.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginResponse {
    private String message;
    private Integer code;
    private UserLoginResponseDTO data;

    //생성 메소드//
    public static UserLoginResponse UserResponseCreate(String message, Integer code, UserLoginResponseDTO data){
        return UserLoginResponse.builder()
                .message(message)
                .code(code)
                .data(data)
                .build();
    }
}
