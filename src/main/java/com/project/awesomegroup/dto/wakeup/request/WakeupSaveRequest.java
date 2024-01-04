package com.project.awesomegroup.dto.wakeup.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.awesomegroup.dto.alarm.Alarm;
import com.project.awesomegroup.dto.alarmdetail.AlarmDetail;
import com.project.awesomegroup.dto.team.Team;
import com.project.awesomegroup.dto.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Builder
public class WakeupSaveRequest {

    @Schema(description = "미션 성공 여부", nullable = false, example = "true")
    private boolean success;

    @Schema(description = "기상 설정 날짜", nullable = false, example = "2023-12-30", type = "string")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date datetime;

    @Schema(description = "기상 설정 시간", nullable = false, example = "06")
    private Integer wakeupHour;

    @Schema(description = "기상 설정 분", nullable = false, example = "30")
    private Integer wakeupMinute;

    @Schema(description = "설정한 메모", nullable = true, example = "Memo")
    private String wakeupMemo;

    @Schema(description = "기상 예보 설정 여부", nullable = false, example = "true")
    private boolean wakeupForecast;

    @Schema(description = "기상 음성 지원 설정 여부", nullable = false, example = "true")
    private boolean wakeupVoice;

    @Schema(description = "유저 아이디", nullable = false, example = "user1")
    private String userId;

    @Schema(description = "팀 아이디", nullable = false, example = "1")
    private Integer teamId;

    @Schema(description = "알람 아이디", nullable = false, example = "1")
    private Integer alarmId;

    @Schema(description = "개인 알람 아이디", nullable = false, example = "1")
    private Integer alarmDetailId;
}
