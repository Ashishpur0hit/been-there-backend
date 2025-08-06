package com.example.been_there.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDTO {
    private Long commentId;
    private String username;
    private String content;
    private String userProfile;
    private String timestamp;
}
