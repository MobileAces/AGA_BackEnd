package com.project.awesomegroup.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.awesomegroup.dto.teammember.TeamMember;
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
    private String userNickname;

    @Column
    private String userPhone;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<TeamMember> teamMemberList = new ArrayList<>();

    @Builder
    public User(String userId, String userPw, String userNickname, String userPhone) {
        this.userId = userId;
        this.userPw = userPw;
        this.userNickname = userNickname;
        this.userPhone = userPhone;
    }

    public User(String id) {
        this.userId = id;
    }
}
