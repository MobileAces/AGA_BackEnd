package com.project.awesomegroup.repository;

import com.project.awesomegroup.dto.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserId(String id);
    Optional<User> findByUserPhone(String phone);
    Optional<User> findByUserNickname(String nickname);
}
