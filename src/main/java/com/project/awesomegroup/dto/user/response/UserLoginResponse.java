package com.project.awesomegroup.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginResponse {
    @Schema(description = "상태 메세지", nullable = false, example = "Login Success")
    private String message;
    @Schema(description = "상태 코드", nullable = false, example = "200")
    private Integer code;
    @Schema(description = "데이터", nullable = false)
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
