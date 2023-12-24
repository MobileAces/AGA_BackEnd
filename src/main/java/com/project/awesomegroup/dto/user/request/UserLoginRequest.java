package com.project.awesomegroup.dto.user.request;

import lombok.Builder;
import lombok.Data;

@Data
public class UserLoginRequest {

    private String userId;
    private String userPw;

    public UserLoginRequest(){}

    @Builder
    public UserLoginRequest(String userId, String userPw){
        this.userId = userId;
        this.userPw = userPw;
    }
}
