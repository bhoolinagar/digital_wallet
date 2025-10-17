package com.batuaa.UserProfile.controller;

import com.batuaa.UserProfile.Dto.BuyerDto;

import com.batuaa.UserProfile.model.Role;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
//@RequiredArgsConstructor
@RequestMapping("buyers/api/v2")
public class BuyerController {

    /*
     * Register Account(User user) Login(String email, String password)+
     * CreateWallet() Logout getAccountByUserId(String userId)
     */


    @PostMapping("/register")
//Register a new user and create wallet automatically
    public ResponseEntity<?> registerUser(@Valid @RequestBody BuyerDto buyerDto) {
       return ResponseEntity.ok(" register successfully");
    }


/*Login user with email, password, and role
Using GET for local testing; in production, should use POST with @RequestBody for security.*/

    @GetMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String emailId, @RequestParam String password, @RequestParam Role role) {

        // Call the service once and get the User
        return ResponseEntity.ok("login successfully");
    }

}