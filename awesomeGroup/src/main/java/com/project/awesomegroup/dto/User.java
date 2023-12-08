package com.project.awesomegroup.dto;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import java.sql.Date;

@Data
@Entity
@NoArgsConstructor
@Table(name="user")
public class User{

    @Id
    private String userId;

    @Column
    private String userPw;

    @Column
    private String userName;

    @Column
    private String userPhone;

    @Column
    private String userAddress;

    @Column
    private String userEmail;

    @Column
    private String userAge;

    @Column
    private Date userBirth;

    @Column
    private String userGender;

    @Builder
    public User(String userId, String userPw, String userName, String userPhone
            , String userAddress, String userEmail, String userAge,
                Date userBirth, String userGender) {
        this.userId = userId;
        this.userPw = userPw;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userAddress = userAddress;
        this.userEmail = userEmail;
        this.userAge = userAge;
        this.userBirth = userBirth;
        this.userGender = userGender;
    }

}
