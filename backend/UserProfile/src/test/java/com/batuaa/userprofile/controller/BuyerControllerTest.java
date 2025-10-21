package com.batuaa.userprofile.controller;

import com.batuaa.userprofile.Dto.BuyerDto;

import com.batuaa.userprofile.model.Buyer;
import com.batuaa.userprofile.model.Role;
import com.batuaa.userprofile.service.BuyerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BuyerController.class)
class BuyerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BuyerService buyerService;

    @Autowired
    private ObjectMapper objectMapper;

    private BuyerDto buyerDto;
    private Buyer savedBuyer;

    @BeforeEach
    void setup() {
        buyerDto = new BuyerDto();
        buyerDto.setEmailId("anjali@gmail.com");
        buyerDto.setName("anjali");
        buyerDto.setPassword("pass@1234");
        buyerDto.setRole(Role.BUYER);

        savedBuyer = new Buyer();
        savedBuyer.setEmailId("anjali@gmail.com");
        savedBuyer.setName("anjali");
        savedBuyer.setPassword("pass@1234");
        savedBuyer.setRole(Role.BUYER);
    }


//    @Test
//    void testRegisterBuyer_Success() throws Exception {
//        // Arrange
//        BuyerDto buyerDto = new BuyerDto();
//        buyerDto.setEmailId("anjali@gmail.com");
//        buyerDto.setName("anjali");
//        buyerDto.setPassword("pass@1234");
//
//        Buyer savedBuyer = new Buyer();
//        savedBuyer.setEmailId("anjali@gmail.com");
//        savedBuyer.setName("Anjali");
//        savedBuyer.setPassword("pass@1234");
//        savedBuyer.setRole(Role.BUYER); //
//
//        when(buyerService.registerBuyer(any(BuyerDto.class))).thenReturn(savedBuyer);
//
//        // Act & Assert
//        mockMvc.perform(post("/buyers/api/v1/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(buyerDto)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.status").value("success"))
//                .andExpect(jsonPath("$.data.emailId").value("anjali@gmail.com"))
//                .andExpect(jsonPath("$.data.role").value("BUYER"));
//    }



    @Test
    void testRegisterBuyer_EmailAlreadyExists() throws Exception {
        when(buyerService.registerBuyer(any(BuyerDto.class)))
                .thenThrow(new RuntimeException("Email already registered"));

        mockMvc.perform(post("/buyers/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buyerDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("fail")))
                .andExpect(jsonPath("$.message", is("Email already registered")));
    }

    //LOGIN
    @Test
    void testLoginBuyer_Success() throws Exception {
        when(buyerService.validateBuyer(any(BuyerDto.class))).thenReturn(savedBuyer);

        mockMvc.perform(post("/buyers/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buyerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.message", is("Buyer login successful")))
                .andExpect(jsonPath("$.data.token", notNullValue()))
                .andExpect(jsonPath("$.data.role", is("BUYER")));
    }

    @Test
    void testLoginBuyer_InvalidCredentials() throws Exception {
        when(buyerService.validateBuyer(any(BuyerDto.class))).thenReturn(null);

        mockMvc.perform(post("/buyers/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buyerDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status", is("fail")))
                .andExpect(jsonPath("$.message", is("Invalid credentials")));
    }
}
