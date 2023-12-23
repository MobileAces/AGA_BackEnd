package com.project.awesomegroup.service;

import com.project.awesomegroup.controller.user.UserController;
import com.project.awesomegroup.dto.User;
import com.project.awesomegroup.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> findAll(){
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(e -> users.add(e));

        return users;
    }

    public User join(User user){
        user.setUserPw(passwordEncoder.encode(user.getUserPw()));
        userRepository.save(user);
        return user;
    }

    public User Login(String userId, String userPw){
        Optional<User> userOptional = userRepository.findByUserId(userId);
        return userOptional.filter(user -> passwordEncoder.matches(userPw, user.getUserPw()))
                .orElse(null);
    }

    public Boolean update(User user){
        try { //유저 정보 업데이트
            userRepository.save(user);
            return true;
        }catch (PersistenceException e){
            return false;
        }
    }

    public Boolean delete(String id){
        try{ //유저 삭제
            userRepository.deleteById(id);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public User select(String id){
        Optional<User> user =  userRepository.findById(id);
        return user.orElseGet(() -> new User("유저 정보가 없습니다."));
    }

    public Boolean NicknameDuplicate(String nickname){
        if(userRepository.findByUserNickname(nickname).isPresent()){
            return true;
        }else{
            return false;
        }
    }

    public Boolean PhoneDuplicate(String phone){
        if(userRepository.findByUserPhone(phone).isPresent()){
            return true;
        }else{
            return false;
        }
    }

    public Boolean IdDuplicate(String id){
        if(userRepository.findById(id).isPresent()){
            return true;
        }
        else{
            return false;
        }
    }
}
