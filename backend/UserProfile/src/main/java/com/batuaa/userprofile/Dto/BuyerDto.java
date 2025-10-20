package com.batuaa.userprofile.Dto;

import com.batuaa.userprofile.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class BuyerDto {

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email")
    private String emailId;

    @NotBlank(message = "Name is required")
    @Size(min = 3, message = "Name must be at least 3 characters")
    private String name;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    private Role role;

    public String getEmailId() {
        return emailId;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }


    public void setEmailId(String emailId) {
    this.emailId = emailId != null ? emailId.trim().toLowerCase() : null;
    }
    public void setName(String name) {
        this.name = name != null ? name.trim() : null;
    }

    public void setPassword(String password) {
        this.password = password != null ? password.trim() : null;
    }
//        public void setEmailId(String emailId) {
//        this.emailId = emailId;
//    }
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
