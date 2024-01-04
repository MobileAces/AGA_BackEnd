package com.project.awesomegroup.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCheckResponse {
    @Schema(description = "상태 메세지", nullable = false, example = "Success")
    private String message;
    @Schema(description = "상태 코드", nullable = false, example = "200")
    private Integer code;
    @Schema(description = "데이터", nullable = false)
    private UserBooleanDTO data;

    //생성 메소드
    public static UserCheckResponse createUserCheckResponse(String message, Integer code, UserBooleanDTO data){
        return UserCheckResponse.builder()
                .message(message)
                .code(code)
                .data(data)
                .build();
    }
}