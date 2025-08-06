package com.example.been_there.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.been_there.config.StorageConfig;
import com.example.been_there.dto.CustomApiResponse;
import com.example.been_there.entity.Profile;
import com.example.been_there.entity.User;
import com.example.been_there.repository.ProfileRepository;
import com.example.been_there.request.UpdateProfileRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.*;

@Service
public class ProfileService {


    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    AmazonS3 s3;

    @Value("${cloud.aws.bucket}")
    String bucket;


    public Profile addProfile()
    {
        try {
            Profile profile = Profile
                    .builder()
                    .address("")
                    .profile("")
                    .profileKey("")
                    .coverPhotoKey("")
                    .coverPhoto("")
                    .bio("")
                    .badges(new ArrayList<String>())
                    .followersCount(0)
                    .followingCount(0)
                    .communityCount(0)
                    .build();

            profile.getBadges().add("Bagpacker");

            return profileRepository.save(profile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }


    }


    public CustomApiResponse<Object> updateProfile(Long profileId, UpdateProfileRequest request) {
        System.out.println("Profile Update from Someone");
        Optional<Profile> foundProfile = profileRepository.findById(profileId);
        if(foundProfile.isPresent())
        {

            if(request.getProfile() != null && !request.getProfile().isEmpty())
            {
                if (foundProfile.get().getProfile() != null && !foundProfile.get().getProfile().isEmpty())
                {
                    Boolean imageDeleted = deleteImage(bucket,foundProfile.get().getProfileKey());
                    if(imageDeleted)
                    {
                        System.out.println("Previous Profile Deleted");
                    }
                    else
                    {
                        System.out.println("Cound not delete Previous Profile ");
                    }
                }

                String originalFileName = request.getProfile().getOriginalFilename();
                String newFileName = UUID.randomUUID().toString()+originalFileName.substring(originalFileName.lastIndexOf('.'));


                Boolean imageUploaded = saveImage(request.getProfile(), newFileName);
                if(imageUploaded)
                {
                    System.out.println("New Profile Uploaded");
                    foundProfile.get().setProfile(generateUrl(newFileName));
                    foundProfile.get().setProfileKey(newFileName);
                }
                else
                {
                    System.out.println("Cound not upload new Profile ");
                }
            }



            if(request.getCoverPhoto() != null && !request.getCoverPhoto().isEmpty())
            {
                if (foundProfile.get().getCoverPhoto() != null && !foundProfile.get().getCoverPhoto().isEmpty())
                {
                    Boolean imageDeleted = deleteImage(bucket,foundProfile.get().getCoverPhotoKey());
                    if(imageDeleted)
                    {
                        System.out.println("Previous Cover Deleted");
                    }
                    else
                    {
                        System.out.println("Cound not delete Previous Cover ");
                    }
                }

                String originalFileName = request.getCoverPhoto().getOriginalFilename();
                String newFileName = UUID.randomUUID().toString()+originalFileName.substring(originalFileName.lastIndexOf('.'));


                Boolean imageUploaded = saveImage(request.getCoverPhoto(), newFileName);
                if(imageUploaded)
                {
                    System.out.println("New Cover Uploaded");
                    foundProfile.get().setCoverPhoto(generateUrl(newFileName));
                    foundProfile.get().setCoverPhotoKey(newFileName);
                }
                else
                {
                    System.out.println("Cound not upload new Profile ");
                }
            }



            foundProfile.get().setBio(request.getBio());
            foundProfile.get().setAddress(request.getAddress());



            Profile savedProfile = profileRepository.save(foundProfile.get());

            return CustomApiResponse
                    .builder()
                    .success(true)
                    .message("Profile Updated Succesfully")
                    .body(savedProfile)
                    .build();
        }
        else {

            return CustomApiResponse
                    .builder()
                    .success(false)
                    .message("Profile Not Found")
                    .body(null)
                    .build();
        }
    }



    public String generateUrl(String newFileName)
    {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60 * 24 * 7; // 7 days
        expiration.setTime(expTimeMillis);

        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket, newFileName)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);

        return String.valueOf(s3.generatePresignedUrl(request));

    }





    public Boolean deleteImage(String bucketName, String key) {
        if (s3.doesObjectExist(bucketName, key)) {
            s3.deleteObject(bucketName, key);
            return true;
        } else {
            return false;
        }
    }





    public Boolean saveImage(MultipartFile image,String newFileName)  {
        //get file name
        try {


            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(image.getSize());
            metadata.setContentType(image.getContentType());

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, newFileName, image.getInputStream(), metadata);
            s3.putObject(putObjectRequest);

            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }








    public CustomApiResponse<Object> find(Long profileId)
    {
        Optional<Profile> profile = profileRepository.findById(profileId);
        if(profile.isPresent())
        {
            profile.get().setProfile(generateUrl(profile.get().getProfileKey()));
            profile.get().setCoverPhoto(generateUrl(profile.get().getCoverPhotoKey()));
            return CustomApiResponse.builder()
                    .success(true)
                    .message("Profile Found")
                    .body(profile.get())
                    .build();
        }

        return CustomApiResponse.builder()
                .success(false)
                .message("Profile Not Found")
                .body(null)
                .build();
    }

}
