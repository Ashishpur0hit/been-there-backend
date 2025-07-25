package com.example.been_there.service;

import com.example.been_there.dto.CustomApiResponse;
import com.example.been_there.entity.Post;
import com.example.been_there.entity.User;
import com.example.been_there.repository.PostRepository;
import com.example.been_there.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;


    public CustomApiResponse<Object> addPost(Long userId,Post post)
    {
        try {
            Optional<User> user = userRepository.findById(userId);

            if(user.isPresent())
            {
                post.setPostedAt(LocalDateTime.now());
                post.setComments(new ArrayList<>());
                post.setUser(user.get());
                user.get().getPosts().add(post);
                Post savedPost =postRepository.save(post);
                User updatedUser = userRepository.save(user.get());

                return CustomApiResponse.builder()
                        .success(true)
                        .message("Post Added Succesfully")
                        .body(savedPost)
                        .build();
            }



        } catch (Exception e) {
            return CustomApiResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .body(null)
                    .build();
        }
        return null;
    }



    public String deletePost(Long postId)
    {
        postRepository.deleteById(postId);
        return "Done";
    }

}
