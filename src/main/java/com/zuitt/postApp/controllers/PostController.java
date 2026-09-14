package com.zuitt.postApp.controllers;


import com.zuitt.postApp.models.Post;
import com.zuitt.postApp.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//It handles HTTP request and returns data as HTTP response
@RestController

//Allows request from different domain or origin.
@CrossOrigin
public class PostController {

    //    Controls and delegate the actual post creation process to the service.
    @Autowired
    PostService postService;

    //    HTTP request
    @RequestMapping(value = "/posts", method = RequestMethod.POST)
//    HTTP Response
    public ResponseEntity<Object> createPost(
//            String token
//            Get the JWT token from the Authorization under request header.
            @RequestHeader(value = "Authorization") String stringToken,
//            Request Body
//            Converts JSON data into a Post Object
            @RequestBody Post post
    ){
        postService.createPost(stringToken, post);

        return new ResponseEntity<>("Post created successfully", HttpStatus.CREATED);
    }

    //    s04 Activity
    @GetMapping("/posts")
    public ResponseEntity<Object> getPosts(){
        return  new ResponseEntity<>(postService.getPosts(), HttpStatus.OK);
    }

    //Update post
    @RequestMapping(value = "/posts/{postid}", method = RequestMethod.PUT)
//    @PathVariable to extract values from URI and bind them to the method parameter.
    public ResponseEntity<Object> updatePost(@PathVariable Long postid, @RequestHeader(value = "Authorization") String stringToken, @RequestBody Post post){
        return postService.updatePost(postid,stringToken, post);
    }

    //    Delete post
    @RequestMapping(value = "/posts/{postid}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deletePost(@PathVariable Long postid, @RequestHeader(value = "Authorization") String stringToken){
        return postService.deletePost(postid, stringToken);
    }

    //    s05 Activity
    @GetMapping("/myPosts")
    public ResponseEntity<Object> getUserPosts(@RequestHeader(value = "Authorization") String stringToken){
        Iterable<Post> userPosts = postService.getUserPosts(stringToken);

//        Check if the user has no posts yet.
        if(!userPosts.iterator().hasNext()){
            return new ResponseEntity<>("No posts found", HttpStatus.OK);
        }

        return new ResponseEntity<>(userPosts, HttpStatus.OK);
    }
}