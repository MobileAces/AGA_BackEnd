package com.project.awesomegroup.service;

import com.project.awesomegroup.controller.user.UserController;
import com.project.awesomegroup.dto.User;
import com.project.awesomegroup.repository.UserRepository;
import jakarta.persistence.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll(){
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(e -> users.add(e));

        return users;
    }

    public User join(User user){
//        logger.info("${}", user);
        userRepository.save(user);
        return user;
    }

    public Boolean update(User user){
        try {//유저 정보 업데이트
            userRepository.save(user);
            return true;
        }catch (PersistenceException e){
            return false;
        }
    }
}
