package com.example.been_there.repository;

import com.example.been_there.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {

    @Query("SELECT p FROM Post p WHERE p.user.id = ?1")
    Optional<List<Post>> findByUserId(Long userId);

}
