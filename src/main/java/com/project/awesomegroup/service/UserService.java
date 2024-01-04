package com.project.awesomegroup.service;

import com.project.awesomegroup.controller.user.UserController;
import com.project.awesomegroup.dto.user.User;
import com.project.awesomegroup.dto.user.request.UserPwRequest;
import com.project.awesomegroup.dto.user.request.UserUpdateRequest;
import com.project.awesomegroup.dto.user.response.*;
import com.project.awesomegroup.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager; //update를 위한 EntityManager 선언

    public UserService(UserRepository userRepository, EntityManager entityManager, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.entityManager = entityManager;
        this.passwordEncoder = passwordEncoder;
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

    @Transactional
    public ResponseEntity<UserResponse> join(User user){
        //사용자가 이미 존재하는지 확인
        if (userRepository.existsById(user.getUserId())){
            //사용자가 이미 있다면 Failed 반환
            UserResponse response = UserResponse.createUserResponse("Sign-up Failed", 400, null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        //회원가입 진행 (사용자가 없을 경우)
        user.setUserPw(passwordEncoder.encode(user.getUserPw()));
        userRepository.save(user);
        //회원가입 성공 응답 반환
        UserResponse response = UserResponse.createUserResponse("Sign-up Success", 201, UserResponseDTO.createUserResponseDTO(user));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<UserLoginResponse> Login(String userId, String userPw) {
        Optional<User> userOptional = userRepository.findByUserId(userId); //사용자 정보 받아오기
        User checkUser = userOptional.filter(user -> passwordEncoder.matches(userPw, user.getUserPw())) //PW 확인
                .orElse(null);
        if(checkUser != null){ //Login Success
            UserLoginResponse response = UserLoginResponse.createUserResponse("Login Success", 200, UserLoginResponseDTO.createUserLoginResponseDTO(checkUser.getUserId()));
            return ResponseEntity.ok(response);
        }
        //Login Fail
        UserLoginResponse response = UserLoginResponse.createUserResponse("Login Failed", 401, null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @Transactional
    public ResponseEntity<UserResponse> update(UserUpdateRequest user){
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
                UserResponse response = UserResponse.createUserResponse("Success", 200, UserResponseDTO.createUserResponseDTO(checkUser.get(), Nickname, Phone));
                return ResponseEntity.ok(response);
            }catch (PersistenceException e){
                UserResponse response = UserResponse.createUserResponse("Fail", 500, null);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }
        UserResponse response = UserResponse.createUserResponse("User not Found", 404, null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @Transactional
    public ResponseEntity<UserPwResponse> passwordUpdate(UserPwRequest request){
        // 사용자 ID 및 비밀번호 검증 (값이 비어있는지 확인)
        if (StringUtils.isEmpty(request.getUserId()) || StringUtils.isEmpty(request.getPrePw()) || StringUtils.isEmpty(request.getNewPw())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(UserPwResponse.userPwResponseCreate("User ID, previous password, and new password are required", 400, UserBooleanDTO.createUserBooleanDTO(false)));
        }
        //사용자 조회
        Optional<User> checkUser = userRepository.findById(request.getUserId());
        if(checkUser.isPresent()){ //사용자 ID가 존재한다면
            //prePW가 저장된 비밀번호와 일치할 때
            if(passwordEncoder.matches(request.getPrePw(), checkUser.get().getUserPw())) {
                if (request.getPrePw().equals(request.getNewPw())) { // 두 개 PW 같을 때 (code = 400)
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(UserPwResponse.userPwResponseCreate("Passwords can not be same", 400, UserBooleanDTO.createUserBooleanDTO(false)));
                }
                try {
                    Query query = entityManager.createQuery("UPDATE User u SET u.userPw = :password WHERE u.userId = :userId");
                    query.setParameter("password", passwordEncoder.encode(request.getNewPw()));
                    query.setParameter("userId", request.getUserId());
                    query.executeUpdate();
                    return ResponseEntity.ok(UserPwResponse.userPwResponseCreate("Success", 200, UserBooleanDTO.createUserBooleanDTO(true)));
                } catch (PersistenceException e) {
                    //service 단에서 에러가 발생한 경우 (code = 500)
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(UserPwResponse.userPwResponseCreate("Server Error", 500, UserBooleanDTO.createUserBooleanDTO(false)));
                }
            }
            else{return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(UserPwResponse.userPwResponseCreate("Password No Match", 400, UserBooleanDTO.createUserBooleanDTO(false)));}
        }
        //유저 ID가 없을 때 (code = 404)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserPwResponse.userPwResponseCreate("User not Found", 404, UserBooleanDTO.createUserBooleanDTO(false)));
    }

    @Transactional
    public ResponseEntity<UserCheckResponse> delete(String userId){
        Optional<User> findUser = userRepository.findByUserId(userId);
        if(findUser.isEmpty()){ //유저 ID가 없을 때 (code = 404)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserCheckResponse.createUserCheckResponse("User not Found", 404, UserBooleanDTO.createUserBooleanDTO(false)));
        }
        try{//유저 삭제
            userRepository.deleteById(userId);
            //삭제를 정상적으로 실행 했을 때 (code = 200)
            return ResponseEntity.ok(UserCheckResponse.createUserCheckResponse("Success", 200, UserBooleanDTO.createUserBooleanDTO(true)));
        } catch (Exception e){

            //삭제를 못하면 (code = 400)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(UserCheckResponse.createUserCheckResponse("Fail", 400, UserBooleanDTO.createUserBooleanDTO(false)));
        }
    }

    public ResponseEntity<UserResponse> select(String userId){
        Optional<User> checkUser =  userRepository.findById(userId);
        if(checkUser.isPresent()){ //해당 id가 존재할 때
            User user = checkUser.get();
            UserResponse response = UserResponse.createUserResponse("User Found", 200, UserResponseDTO.createUserResponseDTO(user));
            return ResponseEntity.ok(response);
        }else{
            //존재 하지 않을 때
            UserResponse response = UserResponse.createUserResponse("User not Found", 404, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    public ResponseEntity<UserCheckResponse> NicknameDuplicate(String nickname){
        if(userRepository.findByUserNickname(nickname).isPresent()){
            //닉네임을 사용하고 있음
            return ResponseEntity.ok(UserCheckResponse.createUserCheckResponse("Success", 200, UserBooleanDTO.createUserBooleanDTO(true)));
        }
        //닉네임을 사용하고 있지 않음
        return ResponseEntity.ok(UserCheckResponse.createUserCheckResponse("Success", 200, UserBooleanDTO.createUserBooleanDTO(false)));
    }

    public ResponseEntity<UserCheckResponse> PhoneDuplicate(String phone){
        if(userRepository.findByUserPhone(phone).isPresent()){
            //중복된 폰 번호가 있음
            return ResponseEntity.ok(UserCheckResponse.createUserCheckResponse("Success", 200, UserBooleanDTO.createUserBooleanDTO(true)));
        }
        //중복된 폰 번호가 없음
        return ResponseEntity.ok(UserCheckResponse.createUserCheckResponse("Success", 200, UserBooleanDTO.createUserBooleanDTO(false)));

    }

    public ResponseEntity<UserCheckResponse> IdDuplicate(String id){
        if(userRepository.findById(id).isPresent()){
            //중복된 ID 있음
            return ResponseEntity.ok(UserCheckResponse.createUserCheckResponse("Success", 200, UserBooleanDTO.createUserBooleanDTO(true)));
        }
        //중복된 ID 없음
        return ResponseEntity.ok(UserCheckResponse.createUserCheckResponse("Success", 200, UserBooleanDTO.createUserBooleanDTO(false)));

    }
}
