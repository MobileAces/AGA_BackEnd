package com.project.awesomegroup.dto.wather;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LatXLngY {
    public double lat;
    public double lng;

    public double x;
    public double y;

    public LatXLngY(){}
}
