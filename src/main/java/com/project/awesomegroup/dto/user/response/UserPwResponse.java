package com.project.awesomegroup.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class UserPwResponse {
    @Schema(description = "상태 메세지", nullable = false, example = "Success")
    private String message;
    @Schema(description = "상태 코드", nullable = false, example = "200")
    private HttpStatus code;
    @Schema(description = "데이터", nullable = false, example = "true")
    private Boolean data;

    //생성 메소드
    public static UserPwResponse userPwResponseCreate(String message, HttpStatus code, Boolean data){
        return UserPwResponse.builder()
                .message(message)
                .code(code)
                .data(data)
                .build();
    }
}
