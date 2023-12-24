package com.project.awesomegroup.controller.user;

import com.project.awesomegroup.dto.user.User;

import com.project.awesomegroup.dto.user.request.UserLoginRequest;
import com.project.awesomegroup.dto.user.request.UserUpdateRequest;
import com.project.awesomegroup.dto.user.response.UserLoginResponse;
import com.project.awesomegroup.dto.user.response.UserResponse;
import com.project.awesomegroup.service.UserService;
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

    @GetMapping("/entirety")
    public List<User> userAllSelect(){
        return userService.findAll();
        //return new ArrayList<>();
    }

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

    @PutMapping("/password")
    public ResponseEntity<String> userPasswordUpdate(@RequestParam String userId, @RequestParam String prePassword, @RequestParam String newPassword){
        UserResponse findUser = userService.select(userId);
        if(findUser.getCode() == 404){ //유저 ID가 없을 때 (code = 404)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not Found");
        }
        if(prePassword.equals(newPassword)){ // 두 개 PW 같을 때 (code = 400)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Passwords can not be same");
        }
        // PW 변경 하기
        if(userService.passwordUpdate(userId, newPassword)){ // PW 변경 성공 (code = 200)
            return ResponseEntity.ok("Success");
        }
        //service 단에서 에러가 발생한 경우 (code = 500)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error");
    }

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

    @GetMapping("/nickname-duplicate")
    public ResponseEntity<Boolean> userNicknameDuplicate(@RequestParam String userNickname){
        return ResponseEntity.ok(userService.NicknameDuplicate(userNickname));
    }

    @GetMapping("/phone-duplicate")
    public ResponseEntity<Boolean> userPhoneDuplicate(@RequestParam String phoneNumber){
        return ResponseEntity.ok(userService.PhoneDuplicate(phoneNumber));
    }

    @GetMapping("/id-duplicate")
    public ResponseEntity<Boolean> userIdDuplicate(@RequestParam String userId){
        return ResponseEntity.ok(userService.IdDuplicate(userId));
    }

}
