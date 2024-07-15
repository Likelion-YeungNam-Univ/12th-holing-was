package com.example.atm.bounded_context.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
public class UserController {
    @GetMapping("")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok().body("테스트 성공");
    }
}
