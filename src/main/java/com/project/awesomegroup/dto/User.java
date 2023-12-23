package com.project.awesomegroup.dto;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Entity
@NoArgsConstructor
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    public User(String userId, String userPw) {
        this.userId = userId;
        this.userPw = userPw;
    }

}
