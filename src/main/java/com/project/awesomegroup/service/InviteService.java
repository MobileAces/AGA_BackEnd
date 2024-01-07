package com.project.awesomegroup.service;

import com.project.awesomegroup.dto.invate.Invitation;
import com.project.awesomegroup.dto.invate.response.InvitationConfirmResponse;
import com.project.awesomegroup.dto.invate.response.InvitationConfirmResponseDTO;
import com.project.awesomegroup.dto.invate.response.InvitationGenerateResponse;
import com.project.awesomegroup.dto.invate.response.InvitationGenerateResponseDTO;
import com.project.awesomegroup.dto.team.Team;
import com.project.awesomegroup.repository.InviteRespository;
import com.project.awesomegroup.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InviteService {
    private final TeamRepository teamRepository;
    private final InviteRespository inviteRespository;

    public ResponseEntity<InvitationGenerateResponse> generateInviteCode(Integer teamId){
        Optional<Team> team = teamRepository.findById(teamId);
        if(team.isEmpty()){ //팀이 존재 하지 않음
            InvitationGenerateResponse response = InvitationGenerateResponse
                    .createInvitationGenerateResponse("Team Not Found",
                            404,
                            InvitationGenerateResponseDTO
                                    .createInvitationGenerateResponseDTO(null));

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return checkInviteCode(team.get());
    }

    private ResponseEntity<InvitationGenerateResponse> checkInviteCode(Team team){
        InvitationGenerateResponse response;
        String newInviteCode;
        // 요청받은 팀에 초대 코드를 들고온다.
        Optional<Invitation> invitation = inviteRespository.findByTeam_TeamId(team.getTeamId());
        if(invitation.isPresent()){
            if(invitation.get().getExpirationDateTime().isAfter(LocalDateTime.now())){
                //초대 코드 만료 전 (기존에 있는 코드 반환)
                response = InvitationGenerateResponse
                        .createInvitationGenerateResponse("Success", HttpStatus.OK.value(),
                                InvitationGenerateResponseDTO
                                        .createInvitationGenerateResponseDTO(invitation.get().getInviteCode()));
                return ResponseEntity.ok(response);
            }else{
                //초대 코드가 만료된 상태 (새로 발급된 코드 반환)
                Invitation existingInvitation = invitation.get();

                // 영속성 컨텍스트에서도 삭제하기 위해 업데이트
                existingInvitation.getTeam().setInvitation(null);

                inviteRespository.delete(existingInvitation);
                newInviteCode = createInviteCode(team);

                response = InvitationGenerateResponse
                        .createInvitationGenerateResponse("Expiration New Code", HttpStatus.CREATED.value(),
                                InvitationGenerateResponseDTO
                                        .createInvitationGenerateResponseDTO(newInviteCode));
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }
        }else{
            // 팀 초대 코드가 없으면 새로 발급해서 반환
            newInviteCode = createInviteCode(team);

            response = InvitationGenerateResponse
                    .createInvitationGenerateResponse("Success New Code", HttpStatus.CREATED.value(),
                            InvitationGenerateResponseDTO
                                    .createInvitationGenerateResponseDTO(newInviteCode));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
    }

    private String createInviteCode(Team team){
        Invitation invitation = Invitation.createInvitation(
                generateRandomCode(),
                LocalDateTime.now().plusDays(3),
                team
        );
        team.addInvitation(invitation);
        inviteRespository.save(invitation);
        return invitation.getInviteCode();
    }

    private String generateRandomCode() {
        //랜덤 코드 생성
        return UUID.randomUUID().toString().substring(0,10);
    }

    public ResponseEntity<InvitationConfirmResponse> confirmationInviteCode(String inviteCode){
        InvitationConfirmResponse response;

        //초대 코드가 유효한지 확인
        Optional<Invitation> invitation = inviteRespository.findByInviteCode(inviteCode);
        if(invitation.isEmpty()){
            //유효하지 않음
            response = InvitationConfirmResponse.createInvitationConfirmResponse("Invalid Code", HttpStatus.NOT_FOUND.value(),
                    InvitationConfirmResponseDTO.createInvitationGenerateResponseDTO(null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if(invitation.get().getExpirationDateTime().isAfter(LocalDateTime.now())) {
            //초대 코드 날짜가 유효함
            response = InvitationConfirmResponse.createInvitationConfirmResponse("Success", HttpStatus.OK.value(),
                    InvitationConfirmResponseDTO.createInvitationGenerateResponseDTO(invitation.get().getTeam().getTeamId()));
            return ResponseEntity.ok(response);
        }else{
            //초대 코드 날짜가 지남
            response = InvitationConfirmResponse.createInvitationConfirmResponse("Expiration Code", HttpStatus.GONE.value(),
                    InvitationConfirmResponseDTO.createInvitationGenerateResponseDTO(null));
            return ResponseEntity.status(HttpStatus.GONE).body(response);
        }
    }
}
