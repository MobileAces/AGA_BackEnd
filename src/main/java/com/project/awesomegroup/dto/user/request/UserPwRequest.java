package com.project.awesomegroup.dto.user.request;

import lombok.Data;

@Data
public class UserPwRequest {
    private String userId;
    private String prePw;
    private String newPw;
}
