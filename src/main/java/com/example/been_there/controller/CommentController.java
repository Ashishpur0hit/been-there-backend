package com.example.been_there.controller;

import com.example.been_there.dto.CustomApiResponse;
import com.example.been_there.entity.Comment;
import com.example.been_there.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    CommentService commentService ;

    @PostMapping("/add")
    public ResponseEntity<CustomApiResponse<Object>> addComment(@RequestParam Long userId , @RequestParam Long postId , @RequestParam String content)
    {
        CustomApiResponse<Object> res = commentService.addComment(userId,postId,content);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


    @GetMapping("/get")
    public ResponseEntity<CustomApiResponse<Object>> getComments(@RequestParam Long postId)
    {
        CustomApiResponse<Object> res = commentService.getAllComments(postId);
        return new ResponseEntity<>(res,HttpStatus.OK);
    }

}
