package com.example.been_there.repository;

import com.example.been_there.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User>  findByUsernameAndPassword(String username , String password);

}
