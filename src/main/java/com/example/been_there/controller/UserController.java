package com.example.been_there.controller;

import com.example.been_there.dto.CustomApiResponse;
import com.example.been_there.entity.User;
import com.example.been_there.service.UserService;
import jakarta.annotation.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;


    @PostMapping("/signup")
    public ResponseEntity<CustomApiResponse<Object>> add(@RequestBody User user)
    {
        System.out.println("Sing Up Request from "+ user.getUsername());
        CustomApiResponse<Object> response = userService.addUser(user);
        return (response.getSuccess())? new ResponseEntity<>(response, HttpStatus.OK) : new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @GetMapping("/find")
    public ResponseEntity<CustomApiResponse<Object>> getUser(@RequestParam Long userId)
    {
        CustomApiResponse<Object> response = userService.getUser(userId);
        return (response.getSuccess())? new ResponseEntity<>(response,HttpStatus.OK) : new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }



    @PostMapping("/login")
    public ResponseEntity<CustomApiResponse<Object>> login(@RequestBody User user)
    {
        System.out.println("Login Request By : "+user.getUsername());
        CustomApiResponse<Object> response = userService.loginUSer(user);
//        return (response.getSuccess())?new ResponseEntity<>(response,HttpStatus.OK):new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response,HttpStatus.OK);
    }


    @GetMapping("/get-card")
    public ResponseEntity<CustomApiResponse<Object>> getUserCard()
    {
        CustomApiResponse<Object> res = userService.getUserCard();
        return new ResponseEntity<>(res,HttpStatus.OK);
    }
}
