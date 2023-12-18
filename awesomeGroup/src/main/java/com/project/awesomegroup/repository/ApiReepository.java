package com.project.awesomegroup.repository;

import com.project.awesomegroup.dto.teammember.TeamMember;
import com.project.awesomegroup.dto.wather.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiReepository extends JpaRepository<Region, Integer> {
}
