package com.microservice.stock.user.service;

import com.microservice.stock.user.dao.UserRepository;
import com.microservice.stock.user.entity.UserEntity;
import com.microservice.stock.user.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailService emailService;

    @Value("${confirmation.mail.sender}")
    private String sender;
    @Value("${confirmation.mail.subject}")
    private String subject;
    @Value("confirmation.mail.contents")
    private String content;

    @Transactional
    public User authenticateLogin(User user) {
        UserEntity userEntity = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        User userInfo = null;
        if (userEntity != null) {
            userInfo = new User();
            userInfo.setRole(userEntity.getRole());
            userInfo.setUsername(userEntity.getUsername());
        }
        return userInfo;
    }

    @Transactional
    public void signUp(User user) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);
        userRepository.saveAndFlush(userEntity);
        emailService.send(sender, user.getEmail(), subject, content + user.getUsername());
    }

    @Transactional(readOnly = true)
    public User retrieveUserProfile(String username) {
        User userInfo = null;
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity != null) {
            userInfo = new User();
            BeanUtils.copyProperties(userEntity, userInfo);
        }
        return userInfo;
    }

    @Transactional
    public void updateUserProfile(User user) {
        UserEntity userEntity = userRepository.findByUsername(user.getUsername());
        if (StringUtils.isNotBlank(user.getPassword())) {
            userEntity.setPassword(user.getPassword());
        }
        if (StringUtils.isNotBlank(user.getEmail())) {
            userEntity.setEmail(user.getEmail());
        }
        if (StringUtils.isNotBlank(user.getMobile())) {
            userEntity.setMobile(user.getMobile());
        }
        userRepository.saveAndFlush(userEntity);
    }

    @Transactional
    public void activateUserAccount(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity != null) {
            userEntity.setConfirmed("Y");
            userRepository.saveAndFlush(userEntity);
        }
    }

}
