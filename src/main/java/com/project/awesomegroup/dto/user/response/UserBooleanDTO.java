package com.project.awesomegroup.dto.user.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserBooleanDTO {
    private Boolean result;

    //생성 메소드
    public static UserBooleanDTO userBooleanDTOCreate(Boolean result){
        return UserBooleanDTO.builder()
                .result(result)
                .build();
    }
}
