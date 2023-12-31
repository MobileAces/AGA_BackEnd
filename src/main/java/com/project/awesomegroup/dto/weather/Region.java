package com.project.awesomegroup.dto.weather;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Region {

    @Id
    @Column(name = "region_id")
    private Long id; // 지역 순번

    @Column(name = "region_parent")
    private String parentRegion; // 시, 도

    @Column(name = "region_child")
    private String childRegion; // 시, 군, 구

    private int nx; // x좌표

    private int ny; // y좌표

    @Embedded
    private Weather weather; // 지역 날씨 정보

    // 날씨 정보 제외하고 지역 생성
    public Region(Long id, String parentRegion, String childRegion, int nx, int ny) {
        this.id = id;
        this.parentRegion = parentRegion;
        this.childRegion = childRegion;
        this.nx = nx;
        this.ny = ny;
    }

    // 날씨 갱신
    public void updateRegionWeather(Weather weather) {
        this.weather = weather;
    }

    @Override
    public String toString() {
        return parentRegion + " " + childRegion;
    }
}
