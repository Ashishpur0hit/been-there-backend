package com.example.been_there.service;

import com.example.been_there.dto.CustomApiResponse;
import com.example.been_there.dto.UserDTO;
import com.example.been_there.entity.Profile;
import com.example.been_there.entity.User;
import com.example.been_there.repository.UserRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProfileService profileService;

    @Transactional
    public CustomApiResponse<Object> addUser(User user)
    {
            Profile savedProfile = profileService.addProfile();

            user.setProfile(savedProfile);
            user.setCreatedAt(LocalDateTime.now());
            user.setPosts(new ArrayList<>());
            user.setCommunities(new HashSet<>());
            User savedUser = userRepository.save(user);


            UserDTO userDTO = UserDTO.builder()
                    .userId(savedUser.getUserId())
                    .profileId(savedProfile.getProfileId())
                    .email(savedUser.getEmail())
                    .password(savedUser.getPassword())
                    .username(savedUser.getUsername())
                    .build();




            return CustomApiResponse
                    .builder()
                    .success(true)
                    .body(userDTO)
                    .message("User Saved Successfully !")
                    .build();

    }



    public CustomApiResponse<Object> getUser(Long userId)
    {
        try
        {
            Optional<User> user = userRepository.findById(userId);
            if(user.isPresent())
            {
                return CustomApiResponse.builder()
                        .success(true)
                        .body(user.get())
                        .message("User Found!")
                        .build();
            }
            else
            {
                return CustomApiResponse
                        .builder()
                        .success(false)
                        .message("User Not Found !")
                        .body(null)
                        .build();
            }
        } catch (Exception e) {
            return CustomApiResponse
                    .builder()
                    .success(false)
                    .body(null)
                    .message(e.getMessage())
                    .build();
        }
    }


    public CustomApiResponse<Object> loginUSer(User user)
    {
        try {
            Optional<User> foundUser = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
            if(foundUser.isPresent())
            {
                UserDTO userDTO = UserDTO.builder()
                        .userId(foundUser.get().getUserId())
                        .profileId(foundUser.get().getProfile().getProfileId())
                        .email(foundUser.get().getEmail())
                        .password(foundUser.get().getPassword())
                        .username(foundUser.get().getUsername())
                        .build();

                return CustomApiResponse.builder()
                        .success(true)
                        .message("User Found !")
                        .body(userDTO)
                        .build();
            }
            else {
                return CustomApiResponse.builder()
                        .success(false)
                        .message("User Not Found !")
                        .body(null)
                        .build();
            }
        } catch (Exception e) {
            return CustomApiResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .body(null)
                    .build();
        }

    }

}
