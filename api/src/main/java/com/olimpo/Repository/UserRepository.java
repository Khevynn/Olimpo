package com.olimpo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.olimpo.Entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    //optional returns the first value finded 
    Optional<UserEntity> findByRefreshToken(String refreshToken); 
    Optional<UserEntity> findByEmail(String email); 
    Optional<UserEntity> findByEmailAndPassword(String email, String password);
    Optional<UserEntity> findByUsernameAndTag(String username, String tag);
    List<UserEntity> findAllByUsername(String username);
}
