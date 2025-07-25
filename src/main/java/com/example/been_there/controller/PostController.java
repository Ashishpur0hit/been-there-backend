package com.example.been_there.controller;

import com.example.been_there.dto.CustomApiResponse;
import com.example.been_there.entity.Post;
import com.example.been_there.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    PostService postService;

    @PostMapping("/add")
    public ResponseEntity<CustomApiResponse<Object>> addPost(@RequestParam Long userId,@RequestBody Post post)
    {
        CustomApiResponse<Object> response = postService.addPost(userId,post);
        return (response.getSuccess())?new ResponseEntity<>(response, HttpStatus.OK):new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }


    @DeleteMapping("/delete")
    public String delete(@RequestParam Long postId)
    {
        return postService.deletePost(postId);
    }
}
