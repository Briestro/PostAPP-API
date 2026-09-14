package com.zuitt.postApp.services;


import com.zuitt.postApp.config.JwtToken;
import com.zuitt.postApp.models.User;
import com.zuitt.postApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

//@Service marks this class as a service component in  the spring application.
// Contains the application's business logic and act as a bridge between the Controller and Repository Layers.
@Service
public class UserServiceImp implements UserService {

    //  @Autowired automatically inject a UserRepository object into this class.
//  Instead of manually creating a UserRepository object using "new", Spring creates and manages the object for us.
    @Autowired
    private UserRepository userRepository;

    //    Validation of token and extracting of username from string token.
    @Autowired
    JwtToken jwtToken;

    //  Creating a new user by passing the User object to UserRepository
    public void createUser(User user){
        userRepository.save(user);
    }

    //  Search for a user based on their username
    public Optional<User> findByUsername(String username){
//      Optional.ogNullable() converts result into an Optional:
//        if user is found, Optional contains the User object
//        if user is not found, Optional is empty or null.
        return Optional.ofNullable(
                userRepository.findByUsername(username)
        );
    }

    //    Update the authenticated user's password.
    public ResponseEntity<Object> updatePassword(String stringToken, String newPassword){
//        Get the User object using the username extracted from the JWT.
        User user = userRepository.findByUsername(
                jwtToken.getUsernameFromToken(stringToken)
        );

//        Hash the new password before saving.
        String encodedPassword = new BCryptPasswordEncoder().encode(newPassword);

        user.setPassword(encodedPassword);

        userRepository.save(user);

        return new ResponseEntity<>("Password updated successfully", HttpStatus.OK);
    }

    //    Delete the authenticated user, if they have no existing posts.
    public ResponseEntity<Object> deleteUser(String stringToken){
//        Get the User object using the username extracted from the JWT.
        User user = userRepository.findByUsername(
                jwtToken.getUsernameFromToken(stringToken)
        );

//        Prevent deletion if the user has existing posts.
        if(!user.getPosts().isEmpty()){
            return new ResponseEntity<>("Cannot delete user with existing posts", HttpStatus.BAD_REQUEST);
        }

        userRepository.delete(user);

        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }

}