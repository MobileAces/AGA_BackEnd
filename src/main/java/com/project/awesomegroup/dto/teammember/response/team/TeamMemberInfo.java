package com.project.awesomegroup.dto.teammember.response.team;

import com.project.awesomegroup.controller.teammember.TeamMemberController;
import com.project.awesomegroup.dto.team.Team;
import com.project.awesomegroup.dto.teammember.TeamMember;
import lombok.Builder;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class TeamMemberInfo {
    private Integer teamId;
    private String teamName;
    private String teamInfo;
    private String teamMaster;
    //유저 List를 저장하기 위한 변수
    private List<TeamMemberUserList> userLists;

    public static TeamMemberInfo teamMemberInfoCreate(List<TeamMember> teamMemberList){
        //팀 멤버 유저 리스트 형식으로 변환하는 명령어 (ForEach랑 비슷한 개념임)
        List<TeamMemberUserList> userLists = teamMemberList
                .stream()
                .map(TeamMemberUserList::userListCreate)
                .collect(Collectors.toList());

        return TeamMemberInfo.builder()
                .teamId(teamMemberList.get(0).getTeam().getTeamId())
                .teamName(teamMemberList.get(0).getTeam().getTeamName())
                .teamInfo(teamMemberList.get(0).getTeam().getTeamInfo())
                .teamMaster(teamMemberList.get(0).getTeam().getTeamMaster())
                .userLists(userLists)
                .build();
    }
}
