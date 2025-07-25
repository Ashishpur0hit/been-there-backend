package com.example.been_there.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private Long userId;
    private String username;
    private String email;
    private String password;
    private Long profileId;
}
