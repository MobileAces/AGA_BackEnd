package com.project.awesomegroup.controller.wather;


import com.project.awesomegroup.dto.teammember.response.team.TeamMemberTeamListResponse;
import com.project.awesomegroup.dto.wather.Region;
import com.project.awesomegroup.dto.wather.WeatherResponseDTO;
import com.project.awesomegroup.dto.wather.Weather;
import com.project.awesomegroup.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/weather")
@CrossOrigin("*")
@Tag(name = "Weather", description = "Weatherr API")
public class WeatherController {
    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    @Autowired
    WeatherService weatherService;

    private final EntityManager em;

    public WeatherController(EntityManager em) {
        this.em = em;
    }

    @Value("${weatherApi.serviceKey}")
    private String serviceKey;


    @Operation(summary = "날씨 정보 조회", description = "위도, 경도에 해당하는 지역의 날씨 정보를 불러옵니다.")
    @GetMapping
    @Transactional
    public ResponseEntity<WeatherResponseDTO> getRegionWeater(@RequestParam("latitude") Double latitude, @RequestParam("longitude") Double longitude){
        //Id & 주소 값 가져오기
        Map<String, String> regionId = weatherService.getRegionId(latitude, longitude);

        //지역 조회
        Region region = em.find(Region.class, regionId.get("PK"));
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst");

        //날짜 조회(시간)
        ZoneId zoneId = ZoneId.of("Asia/Seoul");
        LocalDateTime now = LocalDateTime.now(zoneId);
        String yyyyMMdd = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int hour = now.getHour();
        int min = now.getMinute();

        if(min<=30){hour -= 1;}
        String hourStr;
        if(hour < 10){
            hourStr = "0" + hour + "00";
        }else{
            hourStr = hour + "00";
        }

        String nx = Integer.toString(region.getNx());
        String ny = Integer.toString(region.getNy());
        String currentChangeTime = now.format(DateTimeFormatter.ofPattern("yy.MM.dd ")) + hour;

        // 기준 시각 조회 자료가 이미 존재하고 있다면 API 요청 없이 기존 자료 그대로 넘김
        Weather prevWeather = region.getWeather();
        if(prevWeather != null && prevWeather.getLastUpdateTime() != null) {
            if(prevWeather.getLastUpdateTime().equals(currentChangeTime)) {
                logger.info("기존 자료를 재사용합니다");
                WeatherResponseDTO dto = WeatherResponseDTO.builder()
                        .weather(prevWeather)
                        .message(regionId.get("ADDRESS")).build();
                return ResponseEntity.ok(dto);
            }
        }
        logger.info("API 요청 발송 >>> 지역: {}, 연월일: {}, 시각: {}", region, yyyyMMdd, hourStr);

        try {
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + serviceKey);
            urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
            urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(yyyyMMdd, "UTF-8")); /*‘21년 6월 28일 발표*/
            urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(hourStr, "UTF-8")); /*06시 발표(정시단위) */
            urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); /*예보지점의 X 좌표값*/
            urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); /*예보지점의 Y 좌표값*/

            URL url = new URL(urlBuilder.toString());
            logger.info("request url: {}", url);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            String data = sb.toString();

            //// 응답 수신 완료 ////
            //// 응답 결과를 JSON 파싱 ////

            Double temp = null;
            Double humid = null;
            Double rainAmount = null;

            JSONObject jObject = new JSONObject(data);
            JSONObject response = jObject.getJSONObject("response");
            JSONObject body = response.getJSONObject("body");
            JSONObject items = body.getJSONObject("items");
            JSONArray jArray = items.getJSONArray("item");

            for(int i = 0; i < jArray.length(); i++) {
                JSONObject obj = jArray.getJSONObject(i);
                String category = obj.getString("category");
                double obsrValue = obj.getDouble("obsrValue");

                switch (category) {
                    case "T1H":
                        temp = obsrValue;
                        break;
                    case "RN1":
                        rainAmount = obsrValue;
                        break;
                    case "REH":
                        humid = obsrValue;
                        break;
                }
            }

            Weather weather = new Weather(temp, rainAmount, humid, currentChangeTime);
            region.updateRegionWeather(weather); // DB 업데이트
            WeatherResponseDTO dto = WeatherResponseDTO.builder()
                    .weather(weather)
                    .message(regionId.get("ADDRESS")).build();
            return ResponseEntity.ok(dto);

        } catch (IOException e) {
            WeatherResponseDTO dto = WeatherResponseDTO.builder()
                    .weather(null)
                    .message("날씨 정보를 불러오는 중 오류가 발생했습니다").build();
            return ResponseEntity.ok(dto);
        }
    }
}
