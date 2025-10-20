package com.batuaa.userprofile.controller;

import com.batuaa.userprofile.Dto.ApiResponse;
import com.batuaa.userprofile.Dto.BuyerDto;

import com.batuaa.userprofile.config.JwtUtil;
import com.batuaa.userprofile.model.Buyer;
import com.batuaa.userprofile.model.Role;
import com.batuaa.userprofile.service.BuyerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
//@RequiredArgsConstructor
@RequestMapping("buyers/api/v1")
public class BuyerController {

    @Autowired
    private BuyerService buyerService;

     //Register Buyer
//    @PostMapping("/register")
//    public ResponseEntity<?> registerBuyer(@Valid @RequestBody BuyerDto buyerDto) {
//        try {
//            Buyer savedBuyer = buyerService.registerBuyer(buyerDto);
//
//            // Map the role from saved entity to DTO
//            buyerDto.setRole(savedBuyer.getRole());
//
//            return new ResponseEntity<>(savedBuyer, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }
    @PostMapping("/register")
    public ResponseEntity<?> registerBuyer(@Valid @RequestBody BuyerDto buyerDto) {
        Buyer savedBuyer = buyerService.registerBuyer(buyerDto);
        buyerDto.setRole(savedBuyer.getRole());
        return new ResponseEntity<>(buyerDto, HttpStatus.CREATED);
    }




    // Login Buyer (Returns JWT Token)
    @PostMapping("/login")
    public ResponseEntity<?> loginBuyer(@RequestBody BuyerDto buyerDto) {
        Buyer buyer = buyerService.validateBuyer(buyerDto);
        if (buyer != null) {
            Map<String, String> tokenData = JwtUtil.generateToken(
                    buyer.getEmailId(),
                    "BUYER" // role
            );
            return new ResponseEntity<>(tokenData, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.FORBIDDEN);
        }
    }
}