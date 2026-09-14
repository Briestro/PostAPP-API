package com.zuitt.postApp.controllers;

import com.zuitt.postApp.exceptions.UserException;
import com.zuitt.postApp.models.User;
import com.zuitt.postApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//Marks this class as a Rest controller
// It handles HTTP request and returns reponse to the client.
@RestController

//Allows request from different origin/domains
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/users/register", method = RequestMethod.POST)

    public ResponseEntity<Object> register(@RequestBody Map<String, String> body) throws UserException{
        String username = body.get("username");

//      Check if the username already exist.
        if(!userService.findByUsername(username).isEmpty()){
            throw new UserException("Username already exists!");
        } else {
            String password = body.get("password");

//           hash the password before saving
            String encodedPassword = new BCryptPasswordEncoder().encode(password);

//            Creation of new user object
            User newUser = new User(username, encodedPassword);

            userService.createUser(newUser);

            return new ResponseEntity<>("User registered successfully.", HttpStatus.CREATED);
        }
    }

    //    Allow an authenticated user to update their password.
    @RequestMapping(value = "/users/update-password", method = RequestMethod.PUT)
    public ResponseEntity<Object> updatePassword(
            @RequestHeader(value = "Authorization") String stringToken,
            @RequestBody Map<String, String> body
    ){
        String newPassword = body.get("newPassword");

        return userService.updatePassword(stringToken, newPassword);
    }

    //    Allow an authenticated user to delete their account, provided they have no existing posts.
    @RequestMapping(value = "/users/delete", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteUser(@RequestHeader(value = "Authorization") String stringToken){
        return userService.deleteUser(stringToken);
    }
}