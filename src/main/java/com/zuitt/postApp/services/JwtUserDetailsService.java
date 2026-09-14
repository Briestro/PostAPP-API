package com.zuitt.postApp.services;

import com.zuitt.postApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

// This allows Spring Security and other part of the application to use this class via dependency injection
@Component
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

//    This method implements the LoadUserByUsername() defined by UserDetails Service
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//      Search the database for a user with the provided username.
        com.zuitt.postApp.models.User user = userRepository.findByUsername(username);

//      Notify spring security that the requested user does not exist.
        if(user == null){
            throw new UsernameNotFoundException("User not found with username " + username );
        }

//      Create Spring Security's UserDetails object using username and password retrieved from DB and format it to represent an authenticated user's security information.
        return  new User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
        );
    }
}
