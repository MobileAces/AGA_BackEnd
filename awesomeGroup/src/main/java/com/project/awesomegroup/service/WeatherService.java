package com.project.awesomegroup.service;


import com.google.maps.model.LatLng;
import com.project.awesomegroup.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

import java.util.HashMap;
import java.util.Map;


@Service
public class WeatherService {

    @Autowired
    WeatherRepository weatherRepository;

    public Map<String, String> getRegionId(double x, double y){
        String[] address = geoAddress(x,y).split(" ");
        if(address[3].endsWith("구")){
            address[2]+=address[3];
        }
        String parent = address[1];
        String child = address[2];

        //System.out.println(parent);
        //System.out.println(child);

        Map<String, String> map = new HashMap<>();
        map.put("PK", weatherRepository.findIdByRegionChildAndRegionParent(child, parent).toString());
        map.put("ADDRESS", parent + " " + child);

        return map;
    }

    public static String geoAddress(double latitude, double longitude) {
        // 발급받은 Google Geocoding API 키를 사용해 GeoApiContext를 초기화합니다.
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyBDcKFWOMbQxU7fJFeQA4x-QdypNNwoC2c") // 여기에 발급받은 API 키를 넣어주세요
                .build();

        // 위도와 경도 지정
        //latitude = 37.671191666666665; // 예시 위도
        //longitude = 126.81209722222222; // 예시 경도

        try {
            // Geocoding API를 사용하여 역 지오코딩을 수행합니다.
            GeocodingResult[] results = GeocodingApi.newRequest(context)
                    .language("ko") // 언어 설정
                    .latlng(new LatLng(latitude, longitude))
                    .await();

            // 결과 확인
            if (results != null && results.length > 0) {
                //System.out.println("주소: " + results[0].formattedAddress);
                return results[0].formattedAddress;
            } else {
                //System.out.println("역 지오코딩 결과 없음");
                return "NOT RESULT";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ERROR";
    }

}
