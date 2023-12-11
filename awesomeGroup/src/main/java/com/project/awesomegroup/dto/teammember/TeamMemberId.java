package com.project.awesomegroup.dto.teammember;

import java.io.Serializable;

public class TeamMemberId implements Serializable {
    private int team;
    private String user;

    @Override
    public int hashCode(){
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj){
        return super.equals(obj);
    }
}
