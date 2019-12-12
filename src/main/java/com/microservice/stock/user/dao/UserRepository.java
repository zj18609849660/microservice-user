package com.microservice.stock.user.dao;

import com.microservice.stock.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsernameAndPassword(String username, String password);

    UserEntity findByUsername(String username);
}
