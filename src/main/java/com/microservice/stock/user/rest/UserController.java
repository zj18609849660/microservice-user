package com.microservice.stock.user.rest;

import com.microservice.stock.user.model.User;
import com.microservice.stock.user.rest.response.LoginResponse;
import com.microservice.stock.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/registration")
    public Map<String, String> signUp(@RequestBody User user) {
        userService.signUp(user);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("signUpStatus", "successful");
        return resultMap;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody User user) {
        LoginResponse loginResponse = new LoginResponse();
        User userDetail = userService.authenticateLogin(user);
        if (userDetail != null) {
            loginResponse.setToken("");
            loginResponse.setRole(userDetail.getRole());
            loginResponse.setUsername(userDetail.getUsername());
            loginResponse.setStatus("successful");
        } else {
            loginResponse.setStatus("login failed");
        }
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity<User> retrieveUserProfile(@PathVariable("username") String username) {
        User userDetails = userService.retrieveUserProfile(username);
        if(userDetails!=null){
            return new ResponseEntity<>(userDetails, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/profile/update")
    public Map<String, String> updateUserProfile(@RequestBody User user) {
        userService.updateUserProfile(user);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("updateProfileStatus", "successful");
        return resultMap;
    }

    @GetMapping("/activation")
    public Map<String,String> activeAccount(@RequestParam("username") String username){
        Map<String, String> resultMap = new HashMap<>();
        userService.activateUserAccount(username);
        resultMap.put("accountActivationStatus", "successful");
        return resultMap;
    }

}
