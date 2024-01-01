package com.project.awesomegroup.controller.user;

import com.project.awesomegroup.dto.user.User;

import com.project.awesomegroup.dto.user.request.UserLoginRequest;
import com.project.awesomegroup.dto.user.request.UserPwRequest;
import com.project.awesomegroup.dto.user.request.UserUpdateRequest;
import com.project.awesomegroup.dto.user.response.UserLoginResponse;
import com.project.awesomegroup.dto.user.response.UserPwResponse;
import com.project.awesomegroup.dto.user.response.UserResponse;
import com.project.awesomegroup.dto.user.response.UserResponseDTO;
import com.project.awesomegroup.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
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
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

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
        UserResponse checkUser = userService.select(userId);
        if(checkUser.getCode() == 200){
            //해당하는 ID 정보를 찾았을 때 (code = 200)
            return ResponseEntity.ok(checkUser);
        }else{
            //정보를 찾지 못했을 때 (code = 404)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(checkUser);
        }
    }

    @Operation(summary = "유저 회원가입", description = "유저 정보를 기입해 유저 정보를 저장합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원가입 성공 (message : \"Sign-up Success\", code : 201)", content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "회원가입 실패 (message : \"Sign-up Failed\", code : 400, data : null)", content = @Content)
    })
    @PostMapping("/sign-up")
    public ResponseEntity<UserResponse> userJoin(@RequestBody User user){
        UserResponse checkUser = userService.join(user);
        if(checkUser.getCode() == 400){
            //회원 가입 실패 시 (사용자 이미 존재함) (code = 400)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(checkUser);
        }
        //회원 가입 성공 시 (code = 201)
        return ResponseEntity.status(HttpStatus.CREATED).body(checkUser);
    }

    @Operation(summary = "유저 로그인", description = "유저의 ID, PW를 입력 받아 로그인을 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = UserLoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "로그인 실패 (message : \"Login Failed\", code : 401, data : null)", content = @Content)
    })
    @PostMapping("/sign-in")
    public ResponseEntity<UserLoginResponse> userLogin(@RequestBody UserLoginRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        UserLoginResponse checkUser = userService.Login(request.getUserId(), request.getUserPw());
        if(checkUser.getCode() == 200){ //로그인 성공 (code = 200)
            //사용자 ID를 Cookie에 저장
            Cookie cookie = new Cookie("loginId", URLEncoder.encode(request.getUserId(), "utf-8"));
            cookie.setMaxAge(1000 * 1000);
            response.addCookie(cookie);
        }else{ //로그인 실패 시 (code = 401)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(checkUser);
        }
        return ResponseEntity.ok(checkUser);
    }

    @Operation(summary = "유저 수정", description = "유저 Nickname, PhoneNumber 입력 된 것만 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공 (message : \"Success\", code : 200)", content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "수정 실패 (message : \"User not Found\", code : 404, data : null)", content = @Content),
            @ApiResponse(responseCode = "500", description = "수정 실패 (message : \"Fail\", code : 500, data : null)", content = @Content)
    })
    @PutMapping
    public ResponseEntity<UserResponse> userUpdate(@RequestBody UserUpdateRequest user){
        UserResponse updateUser = userService.update(user);
        if(updateUser.getCode() == 200){ // 업데이트 성공 (code = 200)
            return ResponseEntity.ok(updateUser);
        }else if(updateUser.getCode() == 404){ // 해당 유저를 찾지 못함 (code = 404)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(updateUser);
        }else{ // service 단에서 에러가 발생한 경우 (code = 500)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(updateUser);
        }
    }

    @Operation(summary = "유저 비밀번호 변경", description = "유저 PW를 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "PW 변경 성공 (message : \"Success\", code : 200, data : true)", content = @Content(schema = @Schema(implementation = String.class))),
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
            @ApiResponse(responseCode = "200", description = "삭제 성공 (string : \"Success\")", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "삭제 실패 (string : \"User not Found\")", content = @Content),
            @ApiResponse(responseCode = "500", description = "삭제 실패 (string : \"Server Error\")", content = @Content)
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> userDelete(@PathVariable String userId){
        UserResponse findUser = userService.select(userId);
        if(findUser.getCode() == 404){ //유저 ID가 없을 때 (code = 404)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not Found");
        }
        if(userService.delete(userId)){ //삭제를 정상적으로 실행 했을 때 (code = 200)
            return ResponseEntity.ok("Success");
        }
        //service 단에서 에러가 발생한 경우 (code = 500)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error");
    }

    @Operation(summary = "유저 닉네임 중복 확인", description = "닉네임을 입력 받아 사용 여부를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용 불가능 : true, 사용 가능 : false", content = @Content(schema = @Schema(implementation = Boolean.class))),
    })
    @GetMapping("/nickname-duplicate")
    public ResponseEntity<Boolean> userNicknameDuplicate(@RequestParam String userNickname){
        return ResponseEntity.ok(userService.NicknameDuplicate(userNickname));
    }

    @Operation(summary = "유저 핸드폰 번호 중복 확인", description = "핸드폰 번호를 입력 받아 존재 여부를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용 불가능 : true, 사용 가능 : false", content = @Content(schema = @Schema(implementation = Boolean.class))),
    })
    @GetMapping("/phone-duplicate")
    public ResponseEntity<Boolean> userPhoneDuplicate(@RequestParam String phoneNumber){
        return ResponseEntity.ok(userService.PhoneDuplicate(phoneNumber));
    }

    @Operation(summary = "유저 아이디 중복 확인", description = "아이디를 입력 받아 사용 여부를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용 불가능 : true, 사용 가능 : false", content = @Content(schema = @Schema(implementation = Boolean.class))),
    })
    @GetMapping("/id-duplicate")
    public ResponseEntity<Boolean> userIdDuplicate(@RequestParam String userId){
        return ResponseEntity.ok(userService.IdDuplicate(userId));
    }

}
