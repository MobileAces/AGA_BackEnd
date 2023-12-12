package com.project.awesomegroup.dto.sign_up.response;

import com.project.awesomegroup.dto.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record SignUpResponse(
        @Schema(description = "회원 고유키", example = "c0a80121-7aeb-4b4b-8b7a-9b9b9b9b9b9b")
        UUID id,
        @Schema(description = "회원 아이디", example = "colabear754")
        String account,
        @Schema(description = "회원 이름", example = "콜라곰")
        String name,
        @Schema(description = "회원 나이", example = "30")
        Integer age
) {
//    public static SignUpResponse from(User user) {
//        return new SignUpResponse(
//                user.getUserId(),
//                user.getAccount(),
//                user.getName(),
//                user.getAge()
//        );
//    }
}
