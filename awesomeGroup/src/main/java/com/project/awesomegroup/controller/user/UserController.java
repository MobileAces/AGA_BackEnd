package com.project.awesomegroup.controller.user;

import com.project.awesomegroup.dto.User;
import com.project.awesomegroup.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/users")
@Tag(name = "User", description = "User API")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @GetMapping("/all")
    public List<User> userAllSelect(){
        return userService.findAll();
        //return new ArrayList<>();
    }

    @GetMapping("/{userId}")
    public User userSelect(@PathVariable String userId){
        return userService.select(userId);
    }

    @PostMapping
    public User userJoin(@RequestBody User user){
        return userService.join(user);
    }

    @PutMapping
    public Boolean userUpdate(@RequestBody User user){
        return userService.update(user);
    }

    @DeleteMapping("/{userId}")
    public Boolean userDelete(@PathVariable String userId){
        return userService.delete(userId);
    }
}
