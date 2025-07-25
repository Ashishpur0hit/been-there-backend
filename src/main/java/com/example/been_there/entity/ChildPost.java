package com.example.been_there.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private String media;
}
