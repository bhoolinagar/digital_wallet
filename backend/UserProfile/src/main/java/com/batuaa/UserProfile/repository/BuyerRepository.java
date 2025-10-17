package com.batuaa.UserProfile.repository;

import com.batuaa.UserProfile.model.Buyer;

import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, String> {
    // For login and registration checks
    Optional<Buyer> findByEmailId(String emailId);

    // For login and registration checks
    boolean existsByEmailIdIgnoreCase(String normalizedEmail);

    Object existsByEmailId(@Email String emailId);


}