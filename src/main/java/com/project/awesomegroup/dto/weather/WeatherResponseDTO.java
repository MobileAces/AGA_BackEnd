package com.project.awesomegroup.dto.weather;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Embedded;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeatherResponseDTO {
    @JsonIgnore
    private String baseData;

    @JsonIgnore
    private String baseDate;

    @JsonIgnore
    private String baseTime;

    @JsonIgnore
    private String category;

    @JsonIgnore
    private int nx;

    @JsonIgnore
    private int ny;

    @JsonIgnore
    private String obsrValue;

    @Embedded
    private Weather weather; // 지역 날씨 정보

    private String message;
}
