package com.example.been_there.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;
    @Column( length = 1000)
    private String coverPhoto;
    private String profileKey;
    private String coverPhotoKey;
    @Column( length = 1000)
    private String profile;
    private String bio;
    private List<String> badges = new ArrayList<>();
    private Integer followersCount;
    private Integer followingCount;
    private Integer communityCount;
    private String address;

    //Gender





}
