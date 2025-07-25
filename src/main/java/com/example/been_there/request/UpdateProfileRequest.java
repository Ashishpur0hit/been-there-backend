package com.example.been_there.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProfileRequest {
    private MultipartFile profile;
    private MultipartFile coverPhoto;
    private String address;
    private String bio;
}
