package com.project.awesomegroup.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name="user")
public class User{

    @Id
    @Column(name = "user_id")
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TeamMember> teamMemberList = new ArrayList<>();

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

    public User(String id) {
        this.userId = id;
    }
}
