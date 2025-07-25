package com.example.been_there.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomApiResponse <T>{
    private Boolean success;
    private String message;
    private T body;
}
