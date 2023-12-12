package com.project.awesomegroup.service;

import com.project.awesomegroup.common.MemberType;
import com.project.awesomegroup.controller.user.UserController;
import com.project.awesomegroup.dto.User;
import com.project.awesomegroup.dto.sign_in.request.SignInRequest;
import com.project.awesomegroup.dto.sign_in.response.SignInResponse;
import com.project.awesomegroup.repository.UserRepository;
import com.project.awesomegroup.service.token.TokenProvider;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;

    public List<User> findAll(){
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(e -> users.add(e));

        return users;
    }

    public User join(User user){
//        logger.info("${}", user);
        user.setUserPw(passwordEncoder.encode(user.getUserPw()));
        userRepository.save(user);
        return user;
    }

    public SignInResponse login(SignInRequest request) {
        Optional<User> user = userRepository.findByUserId(request.account());
        if(user.isPresent() && passwordEncoder.matches(request.password(), user.get().getUserPw())){
            String token = tokenProvider.createToken(String.format("%s:%s", user.get().getUserId(), MemberType.USER));
            return new SignInResponse(user.get().getUserName(), MemberType.USER, token);
        }
        return new SignInResponse("아이디 또는 비밀번호가 일치하지 않습니다.", MemberType.USER, "");
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

    public Boolean duplicate(String id){
        if(userRepository.findById(id).isPresent()){
            return true;
        }else{
            return false;
        }
    }
}
