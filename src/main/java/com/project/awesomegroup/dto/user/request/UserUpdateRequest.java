package com.project.awesomegroup.dto.user.request;

import lombok.Builder;
import lombok.Data;

@Data
public class UserUpdateRequest {
    private String userId;
    private String userNickname;
    private String userPhone;

    @Builder
    public UserUpdateRequest(String userId, String userNickname, String userPhone){
        this.userId = userId;
        this.userNickname = userNickname;
        this.userPhone = userPhone;
    }

}
