package com.blog.blog.service;

import com.blog.blog.models.Post;
import com.blog.blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AuthService authService;

    public void createPost(Post post){
        User user = authService.getCurrentUser().orElseThrow(()->
           new IllegalArgumentException("no user logged in")
        );
        post.setUsername(user.getUsername());
        post.setCreatedAt(Instant.now());
        postRepository.save(post);
    }

    public List<Post> showAllPost(){
        List<Post> postList = postRepository.findAll();
        return postList;
    }

    public Post getSinglePost(Long id){
        Post post = postRepository.findById(id).orElseThrow(()->new IllegalArgumentException("no post"+id));
        return post;
    }
}
