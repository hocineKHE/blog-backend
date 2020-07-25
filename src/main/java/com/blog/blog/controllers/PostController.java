package com.blog.blog.controllers;

import com.blog.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.blog.blog.models.Post;

import java.util.List;

@RestController
@RequestMapping("/api/posts")

public class PostController {

    @Autowired
    private PostService postService;


    @PostMapping("/")
    @CrossOrigin("*")
    public ResponseEntity createPost(@RequestBody Post post){
        postService.createPost(post);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Post>> showAllPost(){
        return new ResponseEntity<>(postService.showAllPost(), HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Post> showSinglePost(@PathVariable @RequestBody Long id){
        return new ResponseEntity<>(postService.getSinglePost(id), HttpStatus.OK);
    }

}
