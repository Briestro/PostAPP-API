package com.zuitt.postApp.services;

// Defines the operations or business logic that can be performed on the User data.

import com.zuitt.postApp.models.User;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface UserService {

    //    Creating and saving a new user.
    void createUser(User user);

    //    Searches for a user using their username.
    Optional<User> findByUsername(String username);

    //    Update the authenticated user's password.
    ResponseEntity<Object> updatePassword(String stringToken, String newPassword);

    //    Delete the authenticated user, if they have no existing posts.
    ResponseEntity<Object> deleteUser(String stringToken);
}