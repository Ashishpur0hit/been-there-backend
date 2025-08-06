package com.example.been_there.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildPostRequest {
    private String caption;
    private MultipartFile media;
}
