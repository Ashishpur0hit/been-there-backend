package com.example.been_there.controller;

import com.example.been_there.dto.CustomApiResponse;
import com.example.been_there.entity.Post;
import com.example.been_there.request.ChildPostRequest;
import com.example.been_there.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    PostService postService;

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> addPost(
            @RequestPart("userId") String userId,
            @RequestPart("caption") String caption,
            @RequestPart("subject") String subject,
            @RequestPart("mainImage") MultipartFile mainImage,
            @RequestPart("childCaptions") List<String> childCaptions,
            @RequestPart("childImages") List<MultipartFile> childImages
    ) {

        System.out.println("Post Api Called");
        CustomApiResponse<Object> res = postService.addPost(Long.valueOf(userId),caption,subject,mainImage,childCaptions,childImages);
        return ResponseEntity.ok().body(res);
    }




    @DeleteMapping("/delete")
    public String delete(@RequestParam Long postId)
    {
        return postService.deletePost(postId);
    }



    @GetMapping("/get")
    public ResponseEntity<CustomApiResponse<Object>> getPosts(@RequestParam Long userId)
    {
        System.out.println("Post neede for userId"+userId);
        CustomApiResponse<Object> response = postService.getAllPostsByuserId(userId);
        return  new ResponseEntity<>(response,HttpStatus.OK);
    }


    @GetMapping("/get-all")
    public ResponseEntity<CustomApiResponse<Object>> getAllPosts()
    {
        System.out.println("Post neede for All");
        CustomApiResponse<Object> response = postService.getAllPosts();
        return  new ResponseEntity<>(response,HttpStatus.OK);
    }


    @GetMapping("/trending")
    public ResponseEntity<CustomApiResponse<Object>> getTrending()
    {
        CustomApiResponse<Object> res = postService.getTrendingPosts();
        return new ResponseEntity<>(res,HttpStatus.OK);
    }



}
