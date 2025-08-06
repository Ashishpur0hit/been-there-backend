package com.example.been_there.service;

import com.example.been_there.dto.CommentDTO;
import com.example.been_there.dto.CustomApiResponse;
import com.example.been_there.entity.Comment;
import com.example.been_there.entity.Post;
import com.example.been_there.entity.User;
import com.example.been_there.repository.CommentRepository;
import com.example.been_there.repository.PostRepository;
import com.example.been_there.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    UserRepository userRepository;


    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;


    @Autowired
    ProfileService profileService;

    public CustomApiResponse<Object> addComment(Long userId,Long postId,String content)
    {
        System.out.println("Comment added "+ content);

        Optional<User> user = userRepository.findById(userId);
        Optional<Post> post = postRepository.findById(postId);

        if(user.get()!=null && post.get()!=null)
        {
            Comment comment = new Comment();
            comment.setCommentedAt(LocalDateTime.now());
            comment.setUser(user.get());
            comment.setPost(post.get());
            comment.setContent(content);


            Comment savedComment = commentRepository.save(comment);

            CommentDTO commentDTO = new CommentDTO();


            return CustomApiResponse.builder()
                    .success(true)
                    .message("Comment Saved")
                    .body(commentDTO)
                    .build();

        }

        return CustomApiResponse.builder()
                .success(false)
                .message("Post Not Found")
                .body(null)
                .build();
    }




    public CustomApiResponse<Object> getAllComments(Long postId)
    {
        List<Comment> comments = commentRepository.getComments(postId);

        if(!comments.isEmpty())
        {
            List<CommentDTO> commentList = new ArrayList<>();

            for(Comment comment : comments)
            {
                CommentDTO item  = new CommentDTO();
                item.setCommentId(comment.getCommentId());
                item.setTimestamp(comment.getCommentedAt().toString());
                item.setUsername(comment.getUser().getUsername());
                item.setContent(comment.getContent());
                item.setUserProfile(profileService.generateUrl(comment.getUser().getProfile().getProfileKey()));

                commentList.add(item);
            }

            return CustomApiResponse.builder()
                    .success(true)
                    .message("Comment List Found")
                    .body(commentList)
                    .build();
        }

        return CustomApiResponse.builder()
                .success(false)
                .message("No Comments yet")
                .body(new ArrayList<>())
                .build();
    }

}
