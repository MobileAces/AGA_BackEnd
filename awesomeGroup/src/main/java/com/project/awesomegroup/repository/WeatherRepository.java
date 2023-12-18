package com.project.awesomegroup.repository;

import com.project.awesomegroup.dto.wather.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends JpaRepository<Region, Integer> {
    Integer findByNxAndNy(int x, int y);
}
