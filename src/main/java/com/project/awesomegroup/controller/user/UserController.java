package com.project.awesomegroup.controller.user;

import com.project.awesomegroup.dto.user.User;

import com.project.awesomegroup.dto.user.request.UserLoginRequest;
import com.project.awesomegroup.dto.user.request.UserPwRequest;
import com.project.awesomegroup.dto.user.request.UserUpdateRequest;
import com.project.awesomegroup.dto.user.response.*;
import com.project.awesomegroup.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/users")
@Tag(name = "User", description = "User API")
@RequiredArgsConstructor
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Operation(summary = "유저 전체 조회", description = "모든 유저의 정보를 반환합니다.")
    @GetMapping("/entirety")
    public List<UserResponseDTO> userAllSelect(){
        return userService.findAll();
    }

    @Operation(summary = "유저 조회", description = "userId에 맞는 유저 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공 (message : \"User Found\", code : 200)", content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "조회 실패 (message : \"User not Found\", code : 404, data : null)", content = @Content)
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> userSelect(@PathVariable String userId){
        return userService.select(userId);
    }

    @Operation(summary = "유저 회원가입", description = "유저 정보를 기입해 유저 정보를 저장합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원가입 성공 (message : \"Sign-up Success\", code : 201)", content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "회원가입 실패 (message : \"Sign-up Failed\", code : 400, data : null)", content = @Content)
    })
    @PostMapping("/sign-up")
    public ResponseEntity<UserResponse> userJoin(@RequestBody User user){
        return userService.join(user);
    }

    @Operation(summary = "유저 로그인", description = "유저의 ID, PW를 입력 받아 로그인을 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = UserLoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "로그인 실패 (message : \"Login Failed\", code : 401, data : null)", content = @Content)
    })
    @PostMapping("/sign-in")
    public ResponseEntity<UserLoginResponse> userLogin(@RequestBody UserLoginRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        ResponseEntity<UserLoginResponse> responseLogin = userService.Login(request.getUserId(), request.getUserPw());
        if(responseLogin.getBody().getCode() == 200){ //로그인 성공 (code = 200)
            //사용자 ID를 Cookie에 저장
            Cookie cookie = new Cookie("loginId", URLEncoder.encode(request.getUserId(), "utf-8"));
            cookie.setMaxAge(1000 * 1000);
            response.addCookie(cookie);
        }else{ //로그인 실패 시 (code = 401)
            return responseLogin;
        }
        return responseLogin;
    }

    @Operation(summary = "유저 수정", description = "유저 Nickname, PhoneNumber 입력 된 것만 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공 (message : \"Success\", code : 200)", content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "수정 실패 (message : \"User not Found\", code : 404, data : null)", content = @Content),
            @ApiResponse(responseCode = "500", description = "수정 실패 (message : \"Fail\", code : 500, data : null)", content = @Content)
    })
    @PutMapping
    public ResponseEntity<UserResponse> userUpdate(@RequestBody UserUpdateRequest user){
        return userService.update(user);
    }

    @Operation(summary = "유저 비밀번호 변경", description = "유저 PW를 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "PW 변경 성공 (message : \"Success\", code : 200, data : true)", content = @Content(schema = @Schema(implementation = UserPwResponse.class))),
            @ApiResponse(responseCode = "400", description = "PW 변경 실패 (message : \"Passwords can not be same\", code : 400, data : false)\n" +
                    "\n" +
                    "PW 변경 실패 (message : \"Password no Match\", code : 400, data : false)\n" +
                    "\n" +
                    "Pw 변경 실패 (message : \"User ID, previous password, and new password are required\", code : 400, data : false)", content = @Content),
            @ApiResponse(responseCode = "404", description = "PW 변경 실패 (message : \"User not Found\", code : 404, data : false)", content = @Content),
            @ApiResponse(responseCode = "500", description = "PW 변경 실패 (message : \"Server Error\", code : 500, data : false)", content = @Content)
    })
    @PostMapping("/password")
    public ResponseEntity<UserPwResponse> userPasswordUpdate(@RequestBody UserPwRequest request){
        // PW 변경 하기
        return userService.passwordUpdate(request);
    }

    @Operation(summary = "유저 삭제", description = "유저 ID를 입력 받아 유저 정보를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공 (message : \"Success\", code : 200, data : true)", content = @Content(schema = @Schema(implementation = UserCheckResponse.class))),
            @ApiResponse(responseCode = "404", description = "삭제 실패 (message : \"User not Found\", code : 404, data : false)", content = @Content),
            @ApiResponse(responseCode = "400", description = "삭제 실패 (message : \"Fail\", code : 400, data : false)", content = @Content)
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<UserCheckResponse> userDelete(@PathVariable String userId){
        return userService.delete(userId);
    }

    @Operation(summary = "유저 닉네임 중복 확인", description = "닉네임을 입력 받아 사용 여부를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용 불가능 : true, 사용 가능 : false", content = @Content(schema = @Schema(implementation = UserCheckResponse.class))),
    })
    @GetMapping("/nickname-duplicate")
    public ResponseEntity<UserCheckResponse> userNicknameDuplicate(@RequestParam String userNickname){
        return userService.NicknameDuplicate(userNickname);
    }

    @Operation(summary = "유저 핸드폰 번호 중복 확인", description = "핸드폰 번호를 입력 받아 존재 여부를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용 불가능 : true, 사용 가능 : false", content = @Content(schema = @Schema(implementation = UserCheckResponse.class))),
    })
    @GetMapping("/phone-duplicate")
    public ResponseEntity<UserCheckResponse> userPhoneDuplicate(@RequestParam String phoneNumber){
        return userService.PhoneDuplicate(phoneNumber);
    }

    @Operation(summary = "유저 아이디 중복 확인", description = "아이디를 입력 받아 사용 여부를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용 불가능 : true, 사용 가능 : false", content = @Content(schema = @Schema(implementation = UserCheckResponse.class))),
    })
    @GetMapping("/id-duplicate")
    public ResponseEntity<UserCheckResponse> userIdDuplicate(@RequestParam String userId){
        return userService.IdDuplicate(userId);
    }

}
