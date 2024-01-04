package com.project.awesomegroup.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {

    @Schema(description = "상태 메세지", nullable = false, example = "Success")
    private String message;
    @Schema(description = "상태 코드", nullable = false, example = "200")
    private Integer code;
    @Schema(description = "데이터", nullable = true)
    private UserResponseDTO data;

    //생성 메소드//
    public static UserResponse createUserResponse(String message, Integer code, UserResponseDTO data){
        return UserResponse.builder()
                .message(message)
                .code(code)
                .data(data)
                .build();
    }
}
