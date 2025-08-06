package com.example.been_there.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDTO {
    private Long postId;
    private String caption;
    private String subject;
    private String mainPostImage;
    private List<ChildDTO> childs;
    private LocalDateTime postedAt;
    private Long userId;
    private String username;
    private String profileImage;

}
