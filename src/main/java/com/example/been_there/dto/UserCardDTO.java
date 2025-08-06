package com.example.been_there.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCardDTO {
    private Long userId;
    private Long profileId;
    private String username;
    private String profileImage;
}
