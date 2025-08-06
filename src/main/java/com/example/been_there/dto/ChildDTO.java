package com.example.been_there.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChildDTO {
    private Long childId;
    private String caption;
    private String childImage;
}
