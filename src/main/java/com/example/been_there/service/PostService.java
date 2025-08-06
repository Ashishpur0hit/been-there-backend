package com.example.been_there.service;

import com.amazonaws.services.s3.AmazonS3;
import com.example.been_there.dto.ChildDTO;
import com.example.been_there.dto.CustomApiResponse;
import com.example.been_there.dto.PostDTO;
import com.example.been_there.entity.ChildPost;
import com.example.been_there.entity.Post;
import com.example.been_there.entity.User;
import com.example.been_there.repository.PostRepository;
import com.example.been_there.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;


    @Autowired
    AmazonS3 s3;


    @Autowired
    ProfileService profileService;


    @Transactional
    public CustomApiResponse<Object> addPost(Long userId, String caption , String subject, MultipartFile mainImage, List<String> cildCaptions , List<MultipartFile> childImages)
    {
        try {
            Optional<User> user = userRepository.findById(userId);

            if(user.isPresent())
            {

                Post post = new Post();
                post.setCaption(caption);
                post.setSubject(subject);

                if(isValidImage(mainImage))
                {
                    String originalName = mainImage.getOriginalFilename();
                    String newFileName = UUID.randomUUID().toString()+originalName.substring(originalName.lastIndexOf('.'));
                    Boolean isSaved  =profileService.saveImage(mainImage,newFileName);

                    if(isSaved)
                    {
                        post.setMediaKey(newFileName);
                        post.setMedia(profileService.generateUrl(newFileName));
                        System.out.println("Main Profile Saved");
                    }
                    else {
                        System.out.println("Parent Post Not Saved");
                    }
                }

                List<ChildPost> childs = new ArrayList<>();
                List<ChildDTO> childDTOS = new ArrayList<>();

                for(int i=0;i<cildCaptions.size();i++)
                {
                    String url = "";
                    String key = "";
                    if(isValidImage(childImages.get(i)))
                    {
                        String originalChildName = childImages.get(i).getOriginalFilename();
                        String newChildFileName = UUID.randomUUID().toString()+originalChildName.substring(originalChildName.lastIndexOf('.'));
                        Boolean isChildSaved=profileService.saveImage(childImages.get(i),newChildFileName);
                        if(isChildSaved)
                        {
                            key=newChildFileName;
                            url= profileService.generateUrl(newChildFileName);

                        }
                        else {
                            System.out.println("Child Post Not Saved");
                        }
                    }
                    ChildPost child = new ChildPost();
                    child.setCaption(cildCaptions.get(i));
                    child.setMedia(url);
                    child.setMediaKey(key);

                    System.out.println("Child Profile Saved");


                    childs.add(child);
                }


                post.setChilds(childs);
                post.setPostedAt(LocalDateTime.now());
                post.setUser(user.get());


                Post p =postRepository.save(post);

                for(int i=0;i<post.getChilds().size();i++)
                {
                    ChildPost child = post.getChilds().get(i);
                    childDTOS.add(ChildDTO.builder().childId(child.getChildPostId()).caption(child.getCaption()).childImage(child.getMedia()).build());
                }


                System.out.println(p);

                PostDTO postDTO = PostDTO.builder()
                        .caption(post.getCaption())
                        .subject(post.getSubject())
                        .childs(childDTOS)
                        .mainPostImage(post.getMedia())
                        .postedAt(post.getPostedAt())
                        .postId(post.getPostId())
                        .userId(post.getUser().getUserId())
                        .build();

                return CustomApiResponse.builder()
                        .success(true)
                        .message("Post Saved Successfully")
                        .body(postDTO)
                        .build();


            }



        } catch (Exception e) {
            return CustomApiResponse.builder()
                    .success(false)
                    .message("Exception :"+e.getMessage())
                    .body(null)
                    .build();
        }
        return CustomApiResponse.builder()
                .success(false)
                .message("Something Went Wrong")
                .body(null)
                .build();
    }


    private boolean isValidImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        // Check if it's our placeholder (empty content with no filename)
        if (file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
            return false;
        }

        // Check content type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return false;
        }

        // Check file size (placeholders will be very small)
        if (file.getSize() < 100) { // Less than 100 bytes is likely a placeholder
            return false;
        }

        return true;
    }




    public String deletePost(Long postId)
    {
        postRepository.deleteById(postId);
        return "Done";
    }




    public CustomApiResponse<Object> getAllPostsByuserId(Long userId)
    {
        Optional<List<Post>> posts  = postRepository.findByUserId(userId);
        if(posts.isPresent())
        {
            Collections.reverse(posts.get());
            List<PostDTO> postDTOList = new ArrayList<>();


            for(int i=0;i<posts.get().size();i++)
            {
                Post currentPost = posts.get().get(i);
                PostDTO dto = new PostDTO();
                dto.setPostedAt(currentPost.getPostedAt());
                dto.setCaption(currentPost.getCaption());
                dto.setPostId(currentPost.getPostId());
                if(currentPost.getMediaKey()!=null && !currentPost.getMediaKey().isEmpty())dto.setMainPostImage(profileService.generateUrl(currentPost.getMediaKey()));
                else dto.setMainPostImage(null);
                dto.setUserId(currentPost.getUser().getUserId());
                dto.setSubject(currentPost.getSubject());

                dto.setProfileImage(profileService.generateUrl(currentPost.getUser().getProfile().getProfileKey()));
                dto.setUsername(currentPost.getUser().getUsername());
                List<ChildDTO> childDTOList = new ArrayList<>();


                for (int j=0;j<currentPost.getChilds().size();j++)
                {
                    ChildPost currentChild = currentPost.getChilds().get(j);
                    ChildDTO childDTO = new ChildDTO();
                    childDTO.setCaption(currentChild.getCaption());
                    if(currentChild.getMediaKey()!=null && !currentChild.getMediaKey().isEmpty())childDTO.setChildImage(profileService.generateUrl(currentChild.getMediaKey()));
                    else childDTO.setChildImage(null);
                    childDTO.setChildId(currentChild.getChildPostId());


                    childDTOList.add(childDTO);
                }


                dto.setChilds(childDTOList);

                postDTOList.add(dto);

            }

            return CustomApiResponse.builder()
                    .success(true)
                    .message("Posts Found")
                    .body(postDTOList)
                    .build();
        }

        return CustomApiResponse.builder()
                .success(false)
                .message("No Posts Found")
                .body(new ArrayList<>())
                .build();
    }



    public CustomApiResponse<Object> getAllPosts()
    {
         List<Post> posts = postRepository.findAll();
        if(!posts.isEmpty())
        {
            Collections.reverse(posts);
            List<PostDTO> postDTOList = new ArrayList<>();


            for(int i=0;i<posts.size();i++)
            {
                Post currentPost = posts.get(i);
                PostDTO dto = new PostDTO();
                dto.setPostedAt(currentPost.getPostedAt());
                dto.setCaption(currentPost.getCaption());
                dto.setPostId(currentPost.getPostId());
                if(currentPost.getMediaKey()==null || currentPost.getMediaKey().isEmpty())
                    dto.setMainPostImage(null);
                else     dto.setMainPostImage(profileService.generateUrl(currentPost.getMediaKey()));
                dto.setUserId(currentPost.getUser().getUserId());
                dto.setSubject(currentPost.getSubject());
                dto.setProfileImage(profileService.generateUrl(currentPost.getUser().getProfile().getProfileKey()));
                dto.setUsername(currentPost.getUser().getUsername());

                List<ChildDTO> childDTOList = new ArrayList<>();


                for (int j=0;j<currentPost.getChilds().size();j++)
                {
                    ChildPost currentChild = currentPost.getChilds().get(j);
                    ChildDTO childDTO = new ChildDTO();
                    childDTO.setCaption(currentChild.getCaption());
                    if(currentChild.getMediaKey()==null || currentChild.getMediaKey().equals(""))
                        childDTO.setChildImage(null);
                    else childDTO.setChildImage(profileService.generateUrl(currentChild.getMediaKey()));
                    childDTO.setChildId(currentChild.getChildPostId());


                    childDTOList.add(childDTO);
                }


                dto.setChilds(childDTOList);

                postDTOList.add(dto);

            }

            return CustomApiResponse.builder()
                    .success(true)
                    .message("Posts Found")
                    .body(postDTOList)
                    .build();
        }

        return CustomApiResponse.builder()
                .success(false)
                .message("No Posts Found")
                .body(new ArrayList<>())
                .build();
    }



    public CustomApiResponse<Object> getTrendingPosts()
    {
        Page<Post> postsPage =  postRepository.findAll(PageRequest.of(0,5));
        List<Post> posts = new ArrayList<>(postsPage.getContent()); // ✅ mutable
        if(!posts.isEmpty())
        {
            Collections.reverse( posts);
            List<PostDTO> postDTOList = new ArrayList<>();


            for(int i=0;i<posts.size();i++)
            {
                Post currentPost = posts.get(i);
                PostDTO dto = new PostDTO();
                dto.setPostedAt(currentPost.getPostedAt());
                dto.setCaption(currentPost.getCaption());
                dto.setPostId(currentPost.getPostId());
                if(currentPost.getMediaKey()==null || currentPost.getMediaKey().isEmpty())
                    dto.setMainPostImage(null);
                else     dto.setMainPostImage(profileService.generateUrl(currentPost.getMediaKey()));
                dto.setUserId(currentPost.getUser().getUserId());
                dto.setSubject(currentPost.getSubject());
                dto.setProfileImage(profileService.generateUrl(currentPost.getUser().getProfile().getProfileKey()));
                dto.setUsername(currentPost.getUser().getUsername());

                List<ChildDTO> childDTOList = new ArrayList<>();


                for (int j=0;j<currentPost.getChilds().size();j++)
                {
                    ChildPost currentChild = currentPost.getChilds().get(j);
                    ChildDTO childDTO = new ChildDTO();
                    childDTO.setCaption(currentChild.getCaption());
                    if(currentChild.getMediaKey()==null || currentChild.getMediaKey().equals(""))
                        childDTO.setChildImage(null);
                    else childDTO.setChildImage(profileService.generateUrl(currentChild.getMediaKey()));
                    childDTO.setChildId(currentChild.getChildPostId());


                    childDTOList.add(childDTO);
                }


                dto.setChilds(childDTOList);

                postDTOList.add(dto);

            }

            return CustomApiResponse.builder()
                    .success(true)
                    .message("Posts Found")
                    .body(postDTOList)
                    .build();
        }

        return CustomApiResponse.builder()
                .success(false)
                .message("No Posts Found")
                .body(new ArrayList<>())
                .build();
    }




    public CustomApiResponse<Object> findPostBYId(Long postId)
    {
        Optional<Post> post= postRepository.findById(postId);
        if(post.isPresent())
        {
            return CustomApiResponse.builder()
                    .success(true)
                    .message("Post Found")
                    .body(post.get())
                    .build();
        }
        return CustomApiResponse.builder()
                .success(true)
                .message("Post Not Found")
                .body(null)
                .build();
    }


}
