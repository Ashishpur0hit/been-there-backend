package com.example.been_there.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long childPostId;
    private  String caption;
    @Column(length = 1000)
    private String media;
    private String mediaKey;

}
