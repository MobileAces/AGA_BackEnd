package com.project.awesomegroup.dto.teammember.response.team;

import java.util.*;

import com.project.awesomegroup.dto.teammember.TeamMember;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeamMemberUserList {
     private String userId;
     private String userNickname;
     private Integer authority;

     public static TeamMemberUserList userListCreate(TeamMember teamMember){
          return TeamMemberUserList.builder()
                  .userId(teamMember.getUser().getUserId())
                  .userNickname(teamMember.getUser().getUserNickname())
                  .authority(teamMember.getAuthority())
                  .build();
     }
}
