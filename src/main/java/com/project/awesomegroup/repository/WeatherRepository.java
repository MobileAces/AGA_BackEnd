package com.project.awesomegroup.repository;

import com.project.awesomegroup.dto.wather.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends JpaRepository<Region, Integer> {
    @Query("SELECT r.id FROM Region r WHERE r.childRegion = :child AND r.parentRegion = :parent")
    Integer findIdByRegionChildAndRegionParent(@Param("child") String child, @Param("parent") String parent);
}
