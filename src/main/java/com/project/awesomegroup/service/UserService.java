package com.project.awesomegroup.service;

import com.project.awesomegroup.controller.user.UserController;
import com.project.awesomegroup.dto.user.User;
import com.project.awesomegroup.dto.user.request.UserUpdateRequest;
import com.project.awesomegroup.dto.user.response.UserLoginResponse;
import com.project.awesomegroup.dto.user.response.UserLoginResponseDTO;
import com.project.awesomegroup.dto.user.response.UserResponse;
import com.project.awesomegroup.dto.user.response.UserResponseDTO;
import com.project.awesomegroup.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final EntityManager entityManager; //update를 위한 EntityManager 선언

    public UserService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<UserResponseDTO> findAll(){
        List<UserResponseDTO> users = new ArrayList<>();
        userRepository.findAll().forEach(e -> {
            users.add(UserResponseDTO.builder()
                    .userId(e.getUserId())
                    .userNickname(e.getUserNickname())
                    .userPhone(e.getUserPhone())
                    .build());
        });
        return users;
    }

    public UserResponse join(User user){
        //사용자가 이미 존재하는지 확인
        if (userRepository.existsById(user.getUserId())){
            //사용자가 이미 있다면 Failed 반환
            return UserResponse.userResponseCreate("Sign-up Failed", 400, null);
        }
        //회원가입 진행 (사용자가 없을 경우)
        user.setUserPw(passwordEncoder.encode(user.getUserPw()));
        userRepository.save(user);
        //회원가입 성공 응답 반환
        return UserResponse.userResponseCreate("Sign-up Success", 201, new UserResponseDTO(user.getUserId(), user.getUserNickname(), user.getUserPhone()));
    }

    public UserLoginResponse Login(String userId, String userPw){
        Optional<User> userOptional = userRepository.findByUserId(userId); //사용자 정보 받아오기
        User checkUser = userOptional.filter(user -> passwordEncoder.matches(userPw, user.getUserPw())) //PW 확인
                .orElse(null);
        if(checkUser != null){ //Login Success
            return UserLoginResponse.UserResponseCreate("Login Success", 200, new UserLoginResponseDTO(checkUser.getUserId()));
        }
        //Login Fail
        return UserLoginResponse.UserResponseCreate("Login Failed", 401, null);
    }

    @Transactional
    public UserResponse update(UserUpdateRequest user){
        Optional<User> checkUser = userRepository.findById(user.getUserId());
        if(checkUser.isPresent()) {
            String Nickname = checkUser.get().getUserNickname();
            String Phone = checkUser.get().getUserPhone();
            try { //유저 정보 업데이트
                if (user.getUserNickname() != null) {
                    Nickname = user.getUserNickname();
                    Query query = entityManager.createQuery("UPDATE User u SET u.userNickname = :newNickname WHERE u.userId = :userId");
                    query.setParameter("newNickname", user.getUserNickname());
                    query.setParameter("userId", user.getUserId());
                    query.executeUpdate();
                }
                if (user.getUserPhone() != null) {
                    Phone = user.getUserPhone();
                    Query query = entityManager.createQuery("UPDATE User u SET u.userPhone = :newPhone WHERE u.userId = :userId");
                    query.setParameter("newPhone", user.getUserPhone());
                    query.setParameter("userId", user.getUserId());
                    query.executeUpdate();
                }
                return UserResponse.userResponseCreate("Success", 200, new UserResponseDTO(checkUser.get().getUserId(), Nickname, Phone));

            }catch (PersistenceException e){
                return UserResponse.userResponseCreate("Fail", 500, null);
            }
        }
        return UserResponse.userResponseCreate("User not Found", 404, null);
    }

    public Boolean passwordUpdate(String userId, String userPw){
        Optional<User> checkUser = userRepository.findById(userId);
        if(checkUser.isPresent()){
            try {
                Query query = entityManager.createQuery("UPDATE User u SET u.userNickname = :password WHERE u.userId = :userId");
                query.setParameter("password", passwordEncoder.encode(userPw));
                query.executeUpdate();
                return true;
            }catch (PersistenceException e){
                return false;
            }
        }
        return false;
    }

    public Boolean delete(String id){
        try{ //유저 삭제
            userRepository.deleteById(id);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public UserResponse select(String id){
        Optional<User> checkUser =  userRepository.findById(id);
        if(checkUser.isPresent()){ //해당 id가 존재할 때
            User user = checkUser.get();
            return UserResponse.userResponseCreate("User Found", 200, new UserResponseDTO(user.getUserId(), user.getUserNickname(), user.getUserPhone()));
        }else{
            //존재 하지 않을 때
            return UserResponse.userResponseCreate("User not Found", 404, null);
        }
    }

    public Boolean NicknameDuplicate(String nickname){
        return userRepository.findByUserNickname(nickname).isPresent();
    }

    public Boolean PhoneDuplicate(String phone){
        return userRepository.findByUserPhone(phone).isPresent();
    }

    public Boolean IdDuplicate(String id){
        return userRepository.findById(id).isPresent();
    }
}
