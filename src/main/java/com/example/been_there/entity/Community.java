package com.example.been_there.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Community {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String createdBy;
    private LocalDateTime createdAt;

    @ManyToMany(mappedBy = "communities", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();
}

