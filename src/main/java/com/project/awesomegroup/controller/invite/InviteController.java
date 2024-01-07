package com.project.awesomegroup.controller.invite;

import com.project.awesomegroup.dto.invate.request.InvitationConfirmRequest;
import com.project.awesomegroup.dto.invate.response.InvitationConfirmResponse;
import com.project.awesomegroup.dto.invate.response.InvitationGenerateResponse;
import com.project.awesomegroup.dto.wakeup.response.WakeupStatisticsResponse;
import com.project.awesomegroup.service.InviteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("invite")
@RequiredArgsConstructor
public class InviteController {
    private final InviteService inviteService;

    @Operation(summary = "초대 코드 발급", description = "팀 ID에 해당하는 유니크한 초대 코드를 발급합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "발급 성공 (message : \"Success\", code : 200)",content = @Content(schema = @Schema(implementation = InvitationGenerateResponse.class))),
            @ApiResponse(responseCode = "201", description = "발급 성공 (message : \"Success New Code\", code : 201)\n" +
                    "\n" +
                    "발급 성공 (message : \"Expiration New Code\", code : 201)", content = @Content),
            @ApiResponse(responseCode = "404", description = "발급 실패 (message : \"Team Not Found\", code : 404, data : null)", content = @Content)
    })
    @PostMapping("creation")
    public ResponseEntity<InvitationGenerateResponse> generationCode(@RequestParam Integer teamId){
        return inviteService.generateInviteCode(teamId);
    }

    @Operation(summary = "초대 코드 승인 및 확인", description = "입력된 초대 코드로 승인 여부를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공 (message : \"Success\", code : 200)", content = @Content(schema = @Schema(implementation = InvitationConfirmResponse.class))),
            @ApiResponse(responseCode = "404", description = "조회 실패 (message : \"Invalid Code\", code : 404, data : null)", content = @Content),
            @ApiResponse(responseCode = "410", description = "조회 실패 (message : \"Expiration Code\", code : 410, data : null)", content = @Content)
    })
    @PostMapping("confirmation")
    public ResponseEntity<InvitationConfirmResponse> confirmationCode(@RequestBody InvitationConfirmRequest request){
        return inviteService.confirmationInviteCode(request.getInviteCode());
    }

}
