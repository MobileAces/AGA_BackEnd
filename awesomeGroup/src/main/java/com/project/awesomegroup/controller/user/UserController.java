package com.project.awesomegroup.controller.user;

import com.project.awesomegroup.dto.User;
import com.project.awesomegroup.dto.wather.WeatherResponseDTO;
import com.project.awesomegroup.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.server.PathParam;
import jakarta.servlet.http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
    public User userSelect(@PathVariable String userId){
        return userService.select(userId);
    }

    @PostMapping("/sign-up")
    public User userJoin(@RequestBody User user){
        return userService.join(user);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<User> userLogin(@RequestBody User user, HttpServletResponse response) throws UnsupportedEncodingException {
        User checkUser = userService.Login(user.getUserId(), user.getUserPw());
        if(checkUser != null){
            Cookie cookie = new Cookie("loginId", URLEncoder.encode(checkUser.getUserId(), "utf-8"));
            cookie.setMaxAge(1000 * 1000);
            response.addCookie(cookie);
        }else{
            User dto = User.builder()
                    .userId("NOT FOUND")
                    .build();
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.ok(user);
        //Docker테스트용 주석
        //Docker테스트용 주석 한번더
        //Docker테스트용 주석 한번더 2
        //Docker테스트용 주석 한번더 3
        //Docker테스트용 주석 한번더 4
    }

    @PutMapping
    public Boolean userUpdate(@RequestBody User user){
        return userService.update(user);
    }

    @DeleteMapping("/{userId}")
    public Boolean userDelete(@PathVariable String userId){
        return userService.delete(userId);
    }

    @GetMapping("/nickname-duplicate")
    public Boolean userNicknameDuplicate(@RequestParam String userNickname){
        return userService.NicknameDuplicate(userNickname);
    }

    @GetMapping("/phone-duplicate")
    public Boolean userPhoneDuplicate(@RequestParam String phoneNumber){
        return userService.PhoneDuplicate(phoneNumber);
    }

    @GetMapping("/id-duplicate")
    public Boolean userIdDuplicate(@RequestParam String userId){
        return userService.IdDuplicate(userId);
    }

}
