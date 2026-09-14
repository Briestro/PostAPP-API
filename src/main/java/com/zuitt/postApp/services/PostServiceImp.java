package com.zuitt.postApp.services;

import com.zuitt.postApp.config.JwtToken;
import com.zuitt.postApp.models.Post;
import com.zuitt.postApp.models.User;
import com.zuitt.postApp.repositories.PostRepository;
import com.zuitt.postApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImp implements PostService {

//    Access with the CRUD operations
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

//    Validation of token and extracting of username from string token.
    @Autowired
    JwtToken jwtToken;

//    Create a new post using the JWT token to identify the user who is creating the post.
    public void createPost(String stringToken, Post post){
//        Extract the username from the JWT token
        User author = userRepository.findByUsername(
                jwtToken.getUsernameFromToken(stringToken)
        );
//     Create a new Post objet that will be saved to the database.
       Post newPost = new Post();

//      Copy the title from the request Post object to the new Post object.
       newPost.setTitle(post.getTitle());
       newPost.setContent(post.getContent());
//     Associate the post with the authenticated user
       newPost.setUser(author);
       postRepository.save(newPost);
    }

    //    s04 Activity
    public Iterable<Post> getPosts(){
//        findAll() Retrieves all records in the database.
        return postRepository.findAll();
    }

//    Update Post
    public ResponseEntity updatePost(Long id, String stringToken, Post post){
//        Retrieve the post to be updated.
        Post postForUpdating = postRepository.findById(id).get();

//        Get the username of the author from the post to be updated.
        String postAuthor = postForUpdating.getUser().getUsername();

//        Get the username from the string token
        String authenticatedUser = jwtToken.getUsernameFromToken(stringToken);
//      Check if the authenticated user is the author of the post
        if(authenticatedUser.equals(postAuthor)){
            postForUpdating.setTitle(post.getTitle());
            postForUpdating.setContent(post.getContent());
//            Update the post
            postRepository.save(postForUpdating);

            return  new ResponseEntity<>("Post updated successfully", HttpStatus.OK);
        } else{
//            Unauthorized response.
            return  new ResponseEntity<>("You are not authorized to edit this post.", HttpStatus.UNAUTHORIZED);
        }
    }

//    Delete post
    public ResponseEntity deletePost(Long id, String stringToken){
        Post postForDeletion = postRepository.findById(id).get();
        String postAuthor = postForDeletion.getUser().getUsername();
        String authenticatedUser = jwtToken.getUsernameFromToken(stringToken);

        if(authenticatedUser.equals(postAuthor)){
            postRepository.deleteById(id);

            return new ResponseEntity<>("Post deleted succesfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("You are not authorize to delete this post", HttpStatus.UNAUTHORIZED);
        }
    }

    //    s05 Activity
//    Retrieve the authenticated user's posts using their JWT.
    public Iterable<Post> getUserPosts(String stringToken){
//        Get the User object using the username extracted from the JWT.
        User user = userRepository.findByUsername(
                jwtToken.getUsernameFromToken(stringToken)
        );

        return user.getPosts();
    }

}
