package com.batuaa.UserProfile.service;


import com.batuaa.UserProfile.model.Buyer;
import com.batuaa.UserProfile.model.Role;

public interface UserService {
    Buyer registerUser(Buyer buyer);
    Buyer login(String emailId, String password, Role role);
}