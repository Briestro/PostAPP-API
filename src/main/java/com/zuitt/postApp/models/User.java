// S02 Activity Solution

package com.zuitt.postApp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Set;

@Entity

@Table(name="users")

public class User {
    // Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;

    @Column
    private String password;

//    Defines a one-to-many relationship between User and Post
//    mappedBy = "user" means that the "user" field in the Post entity manages the relationship.
//    User entity is the parent, post is the related/child entity.
    @OneToMany(mappedBy = "user")
//    Used to store multiple Post objects associated with this user.
    private Set<Post> posts;

//    Prevent the posts field from being included when the User object is converted to JSON.
//    This prevents infinite recursion when User and Post reference each other.
    @JsonIgnore
    public Set<Post> getPosts(){
        return posts;
    }

    // Constructors
    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter for id
    public Long getId() {
        return id;
    }

    // Getter and Setter for Username and Password
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
