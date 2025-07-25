package com.example.been_there.controller;

import com.example.been_there.dto.CustomApiResponse;
import com.example.been_there.entity.Profile;
import com.example.been_there.request.UpdateProfileRequest;
import com.example.been_there.service.ProfileService;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    ProfileService profileService;


    @PostMapping("/update")
    public ResponseEntity<CustomApiResponse<Object>> update(@RequestParam Long profileId ,  @RequestPart(value = "profile", required = false) MultipartFile profile,
                                                            @RequestPart(value = "coverPhoto", required = false) MultipartFile coverPhoto,
                                                            @RequestPart("address") String address,
                                                            @RequestPart("bio") String bio)
    {
        UpdateProfileRequest request = UpdateProfileRequest.builder()
                .profile(profile)
                .coverPhoto(coverPhoto)
                .address(address)
                .bio(bio)
                .build();

        CustomApiResponse<Object> response = profileService.updateProfile(profileId,request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<CustomApiResponse<Object>> find(@RequestParam Long profileId)
    {
        CustomApiResponse<Object> response = profileService.find(profileId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }



}
