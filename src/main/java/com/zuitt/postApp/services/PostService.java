package com.zuitt.postApp.services;

import com.zuitt.postApp.models.Post;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public interface PostService {

//  Create Post
    void createPost(String stringToken, Post post);

    //    s04 Activity
    //    Returns an Iterable<Post> that can be used to traverse the retrieved posts.
    Iterable<Post> getPosts();

//  Update Post
    ResponseEntity updatePost(Long id, String stringToken, Post post);

//    Delete
    ResponseEntity deletePost(Long id, String stringToken);

//    s05 Activity
    //    Returns all posts belonging to the authenticated user.
    Iterable<Post> getUserPosts(String stringToken);

}
