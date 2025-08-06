package com.example.been_there.request;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class PostRequest {
    private String subject;
    private String caption;
    private MultipartFile mainImage;
    private List<String> childCaptions;
    private List<MultipartFile> childImages;
}
